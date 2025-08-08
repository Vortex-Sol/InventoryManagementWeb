package vortex.imwp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vortex.imwp.Services.CategoryService;
import vortex.imwp.Services.ItemService;
import vortex.imwp.Services.WarehouseService;

@Controller
@RequestMapping(
        path = "/api"
)
public class HomeController {

    private final ItemService itemService;
    private final WarehouseService warehouseService;
    private final CategoryService categoryService;
    public HomeController(ItemService itemService, WarehouseService warehouseService, CategoryService categoryService) {
        this.itemService = itemService;
        this.warehouseService = warehouseService ;
        this.categoryService = categoryService;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("items", itemService.getAll());
        model.addAttribute("quantities", itemService.getQuantitiesForAllItems());
        model.addAttribute("itemWarehouses", itemService.getItemWarehousesMap());
        model.addAttribute("warehouses", warehouseService.getAllWarehouses());
        model.addAttribute("categories", categoryService.getAllCategoryDTOs());
        return "inventory/home";
    }


}
