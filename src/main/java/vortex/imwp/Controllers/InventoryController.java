package vortex.imwp.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import vortex.imwp.DTOs.ItemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vortex.imwp.Models.Item;
import vortex.imwp.Models.Response;
import vortex.imwp.Services.ItemService;
import vortex.imwp.Services.WarehouseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
public class InventoryController {

	WarehouseService warehouseService;
	ItemService itemService;
	public InventoryController(WarehouseService warehouseService, ItemService itemService) {
		this.warehouseService = warehouseService;
		this.itemService = itemService;
	}

	@PostMapping("/add")
	public String addItem(@RequestParam String name,
						  @RequestParam String description,
						  @RequestParam BigDecimal price,
						  @RequestParam(required = false) List<Long> warehouseIds) {
		ItemDTO itemDTO = new ItemDTO(name, description, price);
		if (warehouseIds != null) {
			for (Long wid : warehouseIds) {
				warehouseService.getWarehouseById(wid)
						.ifPresent(itemDTO::addWarehouse);
			}
		}
		itemService.addItem(itemDTO);
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
		model.addAttribute("items", results);
		model.addAttribute("keyword", keyword);
		return "inventory/search-results";
	}
	@GetMapping("/checkout")
	public String inventoryHome() {
		return "inventory/checkout";
	}

	@GetMapping("/edit/{id}")
	public String showEditForm(@PathVariable Long id, Model model) {
		Optional<Item> itemOpt = itemService.getItemById(id);
		if (itemOpt.isEmpty()) {
			return "redirect:/inventory/home";
		}
		ItemDTO dto = itemService.mapToDTO(itemOpt.get());
		model.addAttribute("item", dto);
		model.addAttribute("warehouses", warehouseService.getAllWarehouses());
		return "inventory/edit-item";
	}

	@PostMapping("/update")
	public String updateItem(@RequestParam Long id,
							 @RequestParam String name,
							 @RequestParam String description,
							 @RequestParam BigDecimal price,
							 @RequestParam(required = false) List<Long> warehouseIds) {

		ItemDTO dto = new ItemDTO(id, name, description, price);
		if (warehouseIds != null) {
			for (Long wid : warehouseIds) {
				warehouseService.getWarehouseById(wid).ifPresent(dto::addWarehouse);
			}
		}

		itemService.updateItem(dto);
		return "redirect:/inventory/home";
	}


}
