package vortex.imwp.Controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vortex.imwp.DTOs.ItemDTO;
import vortex.imwp.Models.Receipt;
import vortex.imwp.Models.Sale;
import vortex.imwp.Services.ItemService;
import vortex.imwp.Services.ReceiptService;
import vortex.imwp.Services.SaleService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/receipt")
public class ReceiptController {
	private final ReceiptService receiptService;
	private final SaleService saleService;
	private final ItemService itemService;

	public ReceiptController(ReceiptService receiptService, SaleService saleService, ItemService itemService) {
		this.receiptService = receiptService;
		this.saleService = saleService;
		this.itemService = itemService;
	}

	@GetMapping("/checkout/{saleId}")
	public String checkoutForm(@PathVariable Long saleId, Model model) {
		Sale sale = saleService.getSaleById(saleId);
		double total = sale.getSaleItems().stream()
				.mapToDouble(saleItem -> saleItem.getItem().getPrice().doubleValue() * saleItem.getQuantity())
				.sum();
		model.addAttribute("sale", sale);
		model.addAttribute("totalAmount", total);
		return "inventory/receipt/checkout";
	}


	@GetMapping("/confirm/{receiptId}")
	public String viewReceipt(@PathVariable Long receiptId, Model model) {
		Receipt receipt = receiptService.getReceipt(receiptId);
		model.addAttribute("receipt", receipt);
		return "inventory/receipt/confirmation";
	}
	@GetMapping("/{saleId}/add-items")
	public String showAddItemsPage(@PathVariable Long saleId, Model model) {
		Sale sale = saleService.getSaleById(saleId);
		List<ItemDTO> items = itemService.getAll();

		model.addAttribute("sale", sale);
		model.addAttribute("items", items);
		return "inventory/receipt/add-items";
	}
	@GetMapping("/start-checkout")
	public String startCheckout(@AuthenticationPrincipal UserDetails userDetails) {
		Sale sale = saleService.createSale(userDetails.getUsername());
		return "redirect:/api/receipt/" + sale.getId() + "/add-items";
	}
	@PostMapping("/checkout")
	public String checkout(@RequestParam Long saleId,
						   @RequestParam String paymentMethod,
						   @RequestParam(required = false) BigDecimal amountReceived,
						   Model model) {
		Sale sale = saleService.getSaleById(saleId);

		try {
			Receipt receipt = receiptService.createReceipt(sale, paymentMethod, amountReceived);
			return "redirect:/api/receipt/confirm/" + receipt.getId();
		} catch (IllegalArgumentException e) {
			model.addAttribute("sale", sale);
			model.addAttribute("error", e.getMessage());
			return "inventory/receipt/checkout";
		}
	}
	@PostMapping("/addItem-form")
	public String addItemToSaleForm(@RequestParam Long saleId,
	                                @RequestParam(required = false) Long itemId,
	                                @RequestParam(required = false) String barcode,
	                                @RequestParam int quantity) {

		Long resolvedItemId = null;

		if (barcode != null && !barcode.isBlank()) {
			Optional<ItemDTO> item = itemService.getItemByBarcode(itemId);
			if (item.isEmpty()) {
				return "redirect:/api/receipt/" + saleId + "/add-items?error=ItemNotFound";
			}
			resolvedItemId = item.get().getId();
		} else if (itemId != null) {
			resolvedItemId = itemId;
		} else {
			return "redirect:/api/receipt/" + saleId + "/add-items?error=NoItemSelected";
		}

		saleService.addItemToSale(saleId, resolvedItemId, quantity);
		return "redirect:/api/receipt/" + saleId + "/add-items";
	}




}
