package vortex.imwp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vortex.imwp.dtos.ItemDTO;
import vortex.imwp.models.Receipt;
import vortex.imwp.models.Sale;
import vortex.imwp.models.Warehouse;
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
    private final TaxRateService taxRateService;

	public ReceiptController(ReceiptService receiptService, SaleService saleService,
							 ItemService itemService,WarehouseService warehouseService
							, EmployeeService employeeService, TaxRateService taxRateService) {
		this.receiptService = receiptService;
		this.saleService = saleService;
		this.itemService = itemService;
		this.warehouseService = warehouseService;
		this.employeeService = employeeService;
        this.taxRateService = taxRateService;
	}
	@GetMapping()
	@PreAuthorize("hasAnyRole('SALESMAN','MANAGER','ADMIN', 'SUPERADMIN')")
	public String startCheckout(@AuthenticationPrincipal UserDetails userDetails) {
		Sale sale = saleService.createSale(userDetails.getUsername());
		return "redirect:/api/salesman/" + sale.getId() + "/add-items";
	}

	@PostMapping("/checkout")
	@PreAuthorize("hasAnyRole('SALESMAN','MANAGER','ADMIN', 'SUPERADMIN')")
	public String checkout(@RequestParam Long saleId,
						   @RequestParam String paymentMethod,
						   @RequestParam(required = false) BigDecimal amountReceived,
						   Model model) {
		Sale sale = saleService.getSaleById(saleId);

		try {
			Receipt receipt = receiptService.createReceipt(sale, paymentMethod, amountReceived);
			return "redirect:/api/salesman/confirm/" + receipt.getId();
		} catch (IllegalArgumentException e) {
			populateCheckoutModel(sale, model);
			String msg = (e.getMessage() != null && !e.getMessage().isBlank())
					? e.getMessage()
					: "Amount received must be greater than or equal to the total.";
			model.addAttribute("error", msg);
			model.addAttribute("paymentMethod", paymentMethod);
			model.addAttribute("amountReceived", amountReceived);
			return "inventory/receipt/checkout";
		}
	}
	@GetMapping("/checkout/{saleId}")
	@PreAuthorize("hasAnyRole('SALESMAN','MANAGER','ADMIN', 'SUPERADMIN')")
	public String checkoutForm(@PathVariable Long saleId, Model model) {
		Sale sale = saleService.getSaleById(saleId);
		populateCheckoutModel(sale, model);
		return "inventory/receipt/checkout";
	}


	@GetMapping("/confirm/{receiptId}")
	@PreAuthorize("hasAnyRole('SALESMAN','MANAGER','ADMIN', 'SUPERADMIN')")
	public String viewReceipt(@PathVariable Long receiptId, Model model) {
		Receipt receipt = receiptService.getReceipt(receiptId);
		String receiptJson = receiptService.generateReceiptJson(receipt);

		model.addAttribute("receipt", receipt);
		model.addAttribute("receiptJson", receiptJson);

		return "inventory/receipt/confirmation";
	}

	@GetMapping("/{saleId}/add-items")
	@PreAuthorize("hasAnyRole('SALESMAN','MANAGER','ADMIN', 'SUPERADMIN')")
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
	@PreAuthorize("hasAnyRole('SALESMAN','MANAGER','ADMIN', 'SUPERADMIN')")
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
	@PreAuthorize("hasAnyRole('SALESMAN','MANAGER','ADMIN', 'SUPERADMIN')")
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


    /**
     * SecurityContextHolder gets Authentication to grab the logged-in user's Warehouse ID
     */
	private void populateCheckoutModel(Sale sale, Model model) {
		Map<Long, BigDecimal> itemTotals = new HashMap<>();
		BigDecimal total = BigDecimal.ZERO;
        Optional<Warehouse> warehouse = warehouseService.getWarehouseById(employeeService.getEmployeeByAuthentication(SecurityContextHolder.getContext().getAuthentication()).getWarehouseID());

		sale.getSaleItems().forEach(si -> {
			BigDecimal price = BigDecimal.valueOf(taxRateService.getBrutto(si.getItem(), warehouse.get()) );
			BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(si.getQuantity()));
			itemTotals.put(si.getItem().getId(), itemTotal);
		});

		for (BigDecimal it : itemTotals.values()) {
			total = total.add(it);
		}

		model.addAttribute("sale", sale);
		model.addAttribute("itemTotals", itemTotals);
		model.addAttribute("totalAmount", total);
	}
}
