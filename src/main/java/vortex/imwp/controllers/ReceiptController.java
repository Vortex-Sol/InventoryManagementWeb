package vortex.imwp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vortex.imwp.dtos.ItemDTO;
import vortex.imwp.models.Receipt;
import vortex.imwp.models.Sale;
import vortex.imwp.services.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping("/api/salesman")
public class ReceiptController {
	private final ReceiptService receiptService;
	private final SaleService saleService;
	private final ItemService itemService;
	private final WarehouseService warehouseService;
	private final EmployeeService employeeService;

	public ReceiptController(ReceiptService receiptService, SaleService saleService,
							 ItemService itemService,WarehouseService warehouseService
							, EmployeeService employeeService) {
		this.receiptService = receiptService;
		this.saleService = saleService;
		this.itemService = itemService;
		this.warehouseService = warehouseService;
		this.employeeService = employeeService;
	}
	@GetMapping()
	@PreAuthorize("hasRole('SALESMAN')")
	public String startCheckout(@AuthenticationPrincipal UserDetails userDetails) {
		Sale sale = saleService.createSale(userDetails.getUsername());
		return "redirect:/api/salesman/" + sale.getId() + "/add-items";
	}

	@PostMapping("/checkout")
	@PreAuthorize("hasRole('SALESMAN')")
	public String checkout(@RequestParam Long saleId,
						   @RequestParam String paymentMethod,
						   @RequestParam(required = false) BigDecimal amountReceived,
						   Model model) {
		Sale sale = saleService.getSaleById(saleId);

		try {
			Receipt receipt = receiptService.createReceipt(sale, paymentMethod, amountReceived);
			return "redirect:/api/salesman/confirm/" + receipt.getId();
		} catch (IllegalArgumentException e) {
			model.addAttribute("sale", sale);
			model.addAttribute("error", e.getMessage());
			return "inventory/receipt/checkout";
		}
	}

	@GetMapping("/checkout/{saleId}")
	@PreAuthorize("hasRole('SALESMAN')")
	public String checkoutForm(@PathVariable Long saleId, Model model) {
		Sale sale = saleService.getSaleById(saleId);

		Map<Long, BigDecimal> itemTotals = new HashMap<>();
		AtomicReference<BigDecimal> total = new AtomicReference<>(BigDecimal.ZERO);

		sale.getSaleItems().forEach(saleItem -> {
			BigDecimal price = BigDecimal.valueOf(saleItem.getItem().getPrice());
			BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(saleItem.getQuantity()));
			itemTotals.put(saleItem.getItem().getId(), itemTotal);
			total.set(total.get().add(itemTotal)); //
		});

		model.addAttribute("sale", sale);
		model.addAttribute("totalAmount", total.get());
		model.addAttribute("itemTotals", itemTotals);
		return "inventory/receipt/checkout";
	}


	@GetMapping("/confirm/{receiptId}")
	@PreAuthorize("hasRole('SALESMAN')")
	public String viewReceipt(@PathVariable Long receiptId, Model model) {
		Receipt receipt = receiptService.getReceipt(receiptId);
		String receiptJson = receiptService.generateReceiptJson(receipt);

		model.addAttribute("receipt", receipt);
		model.addAttribute("receiptJson", receiptJson);

		return "inventory/receipt/confirmation";
	}

	@GetMapping("/{saleId}/add-items")
	@PreAuthorize("hasRole('SALESMAN')")
	public String showAddItemsPage(@PathVariable Long saleId,
								   Authentication authentication,
								   Model model) {
		var sale = saleService.getSaleById(saleId);
		var employee = employeeService.getEmployeeByAuthentication(authentication);
		Long warehouseId = employee.getWarehouseID();

		var warehouse = warehouseService.getWarehouseById(warehouseId)
				.orElseThrow(() -> new IllegalStateException("Warehouse not found: " + warehouseId));

		model.addAttribute("sale", sale);
		model.addAttribute("currentWarehouse", warehouse);
		model.addAttribute("itemsFromUserWarehouse", itemService.getItemsByWarehouse(warehouseId));
		model.addAttribute("stockMap", itemService.getQuantitiesForWarehouse(warehouseId));
		return "inventory/receipt/add-items";
	}



	@PostMapping("/addItem-form")
	@PreAuthorize("hasRole('SALESMAN')")
	public String addItemToSaleForm(@RequestParam Long saleId,
									@RequestParam(required = false) Long warehouseId,
									@RequestParam(required = false) Long itemId,
									@RequestParam(required = false) String barcode,
									@RequestParam int quantity,
									Authentication authentication,
									RedirectAttributes redirectAttributes) {
		if (warehouseId == null) {
			warehouseId = employeeService.getEmployeeByAuthentication(authentication).getWarehouseID();
		}

		Long resolvedItemId = null;
		if (barcode != null && !barcode.isBlank()) {
			var item = itemService.getItemByBarcode(Long.parseLong(barcode));
			if (item.isEmpty()) {
				redirectAttributes.addAttribute("error", "Item not found for barcode: " + barcode);
				return "redirect:/api/salesman/" + saleId + "/add-items";
			}
			resolvedItemId = item.get().getId();
		} else if (itemId != null) {
			resolvedItemId = itemId;
		} else {
			redirectAttributes.addAttribute("error", "Please select a product or enter a barcode.");
			return "redirect:/api/salesman/" + saleId + "/add-items";
		}

		try {
			saleService.addItemToSale(saleId, warehouseId, resolvedItemId, quantity);
		} catch (IllegalArgumentException e) {
			redirectAttributes.addAttribute("error", e.getMessage());
		}
		return "redirect:/api/salesman/" + saleId + "/add-items";
	}



	@PostMapping("/cancel/{receiptId}")
	@PreAuthorize("hasRole('SALESMAN')")
	public String cancelReceipt(@PathVariable Long receiptId,
								@AuthenticationPrincipal UserDetails userDetails,
								RedirectAttributes redirectAttributes) {
		try {
			receiptService.cancelReceipt(receiptId, userDetails.getUsername());
			redirectAttributes.addFlashAttribute("success", "Receipt cancelled successfully.");
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error", "Cancellation failed: " + e.getMessage());
		}
		return "redirect:/api/salesman/confirm/" + receiptId;
	}


}
