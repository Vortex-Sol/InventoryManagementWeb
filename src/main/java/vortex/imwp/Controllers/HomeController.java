package vortex.imwp.Controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vortex.imwp.Services.ItemService;
import vortex.imwp.Services.WarehouseService;

import java.nio.file.WatchService;

@RequestMapping(
        path = "/api"
)
public class HomeController {

    private final ItemService itemService;
    private final WarehouseService warehouseService;
    public HomeController(ItemService itemService, WarehouseService warehouseService) {
        this.itemService = itemService;
        this.warehouseService = warehouseService ;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("items", itemService.getAll());
        model.addAttribute("warehouses", warehouseService.getAllWarehouses());
        return "inventory/home";
    }
}
