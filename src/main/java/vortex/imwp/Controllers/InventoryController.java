package vortex.imwp.Controllers;

import org.springframework.http.ResponseEntity;
import vortex.imwp.DTOs.CategoryDTO;
import vortex.imwp.DTOs.ItemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vortex.imwp.Models.Item;
import vortex.imwp.Models.Response;
import vortex.imwp.Models.WarehouseItem;
import vortex.imwp.Services.CategoryService;
import vortex.imwp.Services.ItemService;
import vortex.imwp.Services.WarehouseItemService;
import vortex.imwp.Services.WarehouseService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class InventoryController {

	WarehouseService warehouseService;
	ItemService itemService;
	WarehouseItemService warehouseItemService;
	CategoryService categoryService;
	public InventoryController(WarehouseService warehouseService, ItemService itemService,
							   WarehouseItemService warehouseItemService, CategoryService categoryService) {
		this.warehouseService = warehouseService;
		this.itemService = itemService;
		this.warehouseItemService = warehouseItemService;
		this.categoryService = categoryService;
	}


	@PostMapping("/add")
	public String addItem(@RequestParam String name,
						  @RequestParam String description,
						  @RequestParam double price,
						  @RequestParam Long barcode,
						  @RequestParam(required = false) Long categoryId,
						  @RequestParam(required = false) String newCategoryName,
						  @RequestParam(required = false) List<Long> warehouseIds,
						  @RequestParam(required = false) List<Integer> warehouseQtys) {
		CategoryDTO categoryDTO;

		if (newCategoryName != null && !newCategoryName.trim().isEmpty()) {
			categoryDTO = categoryService.createCategoryIfNotExists(new CategoryDTO(null, newCategoryName.trim()));
		} else if (categoryId != null) {
			categoryDTO = categoryService.getCategoryDTOById(categoryId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid category selected."));
		} else {
			throw new IllegalArgumentException("A category must be selected or created.");
		}

		ItemDTO itemDTO = new ItemDTO(name, description, price, barcode, categoryDTO);
		Item savedItem = itemService.addItem(itemDTO);

		if (warehouseIds != null && warehouseQtys != null && warehouseIds.size() == warehouseQtys.size()) {
			for (int i = 0; i < warehouseIds.size(); i++) {
				Long wid = warehouseIds.get(i);
				Integer qty = warehouseQtys.get(i);

				if (qty == null || qty <= 0) continue;

				warehouseService.getWarehouseById(wid).ifPresent(warehouse -> {
					WarehouseItem wi = new WarehouseItem();
					wi.setItem(savedItem);
					wi.setWarehouse(warehouse);
					wi.setQuantityInStock(qty);
					warehouseItemService.saveWarehouseItem(wi);
				});
			}
		}

		return "redirect:/inventory/home";
	}


	@PostMapping("/delete")
	public String deleteItem(@RequestParam("item_id") Long itemId) {
		itemService.getItemById(itemId).ifPresent(item -> itemService.deleteItem(itemId));
		return "redirect:/inventory/home";
	}

	@GetMapping("/api/items")
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
	public String inventoryHome() {
		return "inventory/checkout";
	}

}
