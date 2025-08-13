package vortex.imwp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vortex.imwp.services.CategoryService;
import vortex.imwp.services.ItemService;
import vortex.imwp.services.WarehouseService;

@Controller
@RequestMapping(path = "/api/warehouse")
public class WarehouseController {

    private final ItemService itemService;
    private final WarehouseService warehouseService;
    private final CategoryService categoryService;
    public WarehouseController(ItemService itemService, WarehouseService warehouseService, CategoryService categoryService) {
        this.itemService = itemService;
        this.warehouseService = warehouseService ;
        this.categoryService = categoryService;
    }

    @GetMapping
    public String inventory(Model model) {
        model.addAttribute("items", itemService.getAll());
        model.addAttribute("quantities", itemService.getQuantitiesForAllItems());
        model.addAttribute("itemWarehouses", itemService.getItemWarehousesMap());
        model.addAttribute("warehouses", warehouseService.getAllWarehouses());
        model.addAttribute("categories", categoryService.getAllCategoryDTOs());
        return "/inventory/items";
    }


}
