package vortex.imwp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vortex.imwp.dtos.ItemDTO;
import vortex.imwp.dtos.CategoryDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import vortex.imwp.models.Employee;
import vortex.imwp.models.Item;
import vortex.imwp.models.Response;
import vortex.imwp.models.WarehouseItem;
import vortex.imwp.services.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/items")
public class InventoryController {

	WarehouseService warehouseService;
	ItemService itemService;
	WarehouseItemService warehouseItemService;
	CategoryService categoryService;
	private final EmployeeService employeeService;
	public InventoryController(WarehouseService warehouseService, ItemService itemService,
							   WarehouseItemService warehouseItemService, CategoryService categoryService, EmployeeService employeeService) {
		this.warehouseService = warehouseService;
		this.itemService = itemService;
		this.warehouseItemService = warehouseItemService;
		this.categoryService = categoryService;
		this.employeeService = employeeService;
	}


	@PostMapping("/add")
	@PreAuthorize("hasAnyRole('STOCKER','MANAGER','ADMIN','SUPERADMIN')")
	public String addItem(@RequestParam String name,
						  @RequestParam String description,
						  @RequestParam double price,
						  @RequestParam Long barcode,
						  @RequestParam(required = false) Long categoryId,
						  @RequestParam(required = false) String newCategoryName,
						  @RequestParam int quantity,
						  @RequestParam(required = false) Long warehouseId,
						  Authentication auth) {

		var employee = employeeService.getEmployeeByAuthentication(auth);

		CategoryDTO categoryDTO = (newCategoryName != null && !newCategoryName.isBlank())
				? categoryService.createCategoryIfNotExists(new CategoryDTO(null, newCategoryName.trim()))
				: categoryService.getCategoryDTOById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid category selected."));

		ItemDTO dto = new ItemDTO(name, description, price, barcode, categoryDTO);
		Item savedItem = itemService.addItem(dto);

		if (quantity > 0) {
			Long effectiveWarehouseId;

			if (employee.getJobs().stream().anyMatch(j -> j.getName().equals("SUPERADMIN"))) {
				effectiveWarehouseId = warehouseId;
			} else {
				effectiveWarehouseId = employee.getWarehouseID();
			}

			if (effectiveWarehouseId != null) {
				var warehouse = warehouseService.getWarehouseById(effectiveWarehouseId)
						.orElseThrow(() -> new IllegalStateException("Warehouse not found"));
				warehouseItemService.saveWarehouseItem(new WarehouseItem(warehouse, savedItem, quantity));
			}
		}

		return "redirect:/api/warehouse";
	}


	@PostMapping("/delete")
	@PreAuthorize("hasAnyRole('STOCKER','MANAGER','ADMIN','SUPERADMIN')")
	public String deleteItem(@RequestParam("item_id") Long itemId, Authentication auth) {
		var employee = employeeService.getEmployeeByAuthentication(auth);

		itemService.getItemById(itemId).ifPresent(item -> {
			if (!employee.getJobs().stream().anyMatch(j -> j.getName().equals("SUPERADMIN"))) {
				var warehouseItems = warehouseItemService.getWarehouseItems(employee.getWarehouseID());
				boolean belongsToWarehouse = warehouseItems.stream()
						.anyMatch(wi -> wi.getItem().getId().equals(itemId));
				if (!belongsToWarehouse) throw new IllegalStateException("Not allowed to delete this item");
			}
			itemService.deleteItem(itemId);
		});

		return "redirect:/api/warehouse";
	}



	@GetMapping()
	@PreAuthorize("hasAnyRole('STOCKER','MANAGER','ADMIN', 'SUPERADMIN')")
	public ResponseEntity<Response> getItems() {
		Response resp = new Response();

		List<ItemDTO> items = itemService.getAll();
		resp.setSuccess(!items.isEmpty());
		resp.setData(items);
		if (resp.isSuccess()) {
			resp.setMessage("Items found");
		} else {
			resp.setMessage("Items not found");
		}
		return ResponseEntity.ok(resp);
	}
	@GetMapping("/search")
	@PreAuthorize("hasAnyRole('STOCKER','MANAGER','ADMIN', 'SUPERADMIN')")
	public String searchItems(@RequestParam("keyword") String keyword, Model model) {
		List<ItemDTO> results = itemService.searchAndMap(keyword);
		Map<Long, Integer> quantities = itemService.getQuantitiesForAllItems();
		Map<Long, List<String>> itemWarehouses = itemService.getItemWarehousesMap();

		model.addAttribute("items", results);
		model.addAttribute("quantities", quantities);
		model.addAttribute("itemWarehouses", itemWarehouses);
		model.addAttribute("warehouses", warehouseService.getAllWarehouses());
		model.addAttribute("keyword", keyword);
		return "inventory/search-results";
	}

	@GetMapping("/checkout")
	@PreAuthorize("hasAnyRole('STOCKER','MANAGER','ADMIN', 'SUPERADMIN')")
	public String inventoryHome() {
		return "inventory/receipt/checkout";
	}

	@GetMapping("/edit/{id}")
	@PreAuthorize("hasAnyRole('STOCKER','MANAGER','ADMIN','SUPERADMIN')")
	public String editItemForm(@PathVariable Long id, Model model) {
		Item item = itemService.getItemById(id)
				.orElseThrow(() -> new IllegalArgumentException("Item not found"));
		model.addAttribute("item", item);
		model.addAttribute("categories", categoryService.getAllCategories());
		return "inventory/edit-item";
	}

	@PostMapping("/edit/{id}")
	@PreAuthorize("hasAnyRole('STOCKER','MANAGER','ADMIN','SUPERADMIN')")
	public String updateItem(@PathVariable Long id,
							 @RequestParam String name,
							 @RequestParam String description,
							 @RequestParam double price,
							 @RequestParam Long barcode,
							 @RequestParam Long categoryId,
							 RedirectAttributes ra) {
		Item item = itemService.getItemById(id)
				.orElseThrow(() -> new IllegalArgumentException("Item not found"));

		item.setName(name);
		item.setDescription(description);
		item.setPrice(price);
		item.setBarcode(barcode);
		item.setCategory(categoryService.getCategoryById(categoryId)
				.orElseThrow(() -> new IllegalArgumentException("Category not found")));

		itemService.updateItem(item);
		ra.addFlashAttribute("toastSuccess", "Item updated");
		return "redirect:/api/warehouse";
	}

}
