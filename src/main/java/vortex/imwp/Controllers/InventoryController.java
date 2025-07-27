package vortex.imwp.Controllers;

import org.springframework.http.ResponseEntity;
import vortex.imwp.DTOs.ItemDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vortex.imwp.Models.Response;
import vortex.imwp.Services.ItemService;
import vortex.imwp.Services.WarehouseService;

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
						  @RequestParam double price,
						  @RequestParam Integer quantity,
						  @RequestParam(required = false) List<Long> warehouseIds) {
		ItemDTO itemDTO = new ItemDTO(name, description, price, quantity);
		if (warehouseIds != null) {
			for (Long wid : warehouseIds) {
				warehouseService.getWarehouseById(wid)
						.ifPresent(itemDTO::addWarehouse);
			}
		}
		itemService.addItem(itemDTO);
		return "redirect:/";
	}

	@GetMapping("/api/items")
	public ResponseEntity<Response> getItems() {
		Response resp = new vortex.imwp.Models.Response();

		Optional<List<ItemDTO>> items = itemService.getAll();
		resp.setSuccess(items.isPresent());
		resp.setData(itemService.getAll());
		if (resp.isSuccess()) resp.setMessage("Items found");
		else resp.setMessage("Items not found");

		return ResponseEntity.ok(resp);
	}
}
