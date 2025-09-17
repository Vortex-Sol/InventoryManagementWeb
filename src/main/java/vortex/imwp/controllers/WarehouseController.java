package vortex.imwp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vortex.imwp.dtos.ItemDTO;
import vortex.imwp.models.Employee;
import vortex.imwp.services.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path = "/api/warehouse")
public class WarehouseController {

    private final ItemService itemService;
    private final WarehouseService warehouseService;
    private final CategoryService categoryService;
    private final EmployeeService employeeService;
    private final WarehouseItemService warehouseItemService;
    public WarehouseController(ItemService itemService, WarehouseService warehouseService,
                               CategoryService categoryService, EmployeeService employeeService
                                , WarehouseItemService warehouseItemService) {
        this.itemService = itemService;
        this.warehouseService = warehouseService ;
        this.categoryService = categoryService;
        this.employeeService = employeeService;
        this.warehouseItemService = warehouseItemService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('STOCKER','MANAGER','ADMIN','SUPERADMIN')")
    public String inventory(Model model, Authentication auth) {
        var employee = employeeService.getEmployeeByAuthentication(auth);

        List<ItemDTO> items;
        Map<Long, Integer> quantities;
        Map<Long, List<String>> itemWarehouses = itemService.getItemWarehousesMap();
        Map<Long, List<Long>> itemWarehouseIds = warehouseItemService.getItemWarehouseIdsMap();



        if (employee.getJobs().stream().anyMatch(j -> j.getName().equals("SUPERADMIN"))) {
            items = itemService.getAll();
            quantities = itemService.getQuantitiesForAllItems();
        } else {
            Long warehouseId = employee.getWarehouseID();
            items = itemService.getItemsByWarehouse(warehouseId);
            quantities = itemService.getQuantitiesForWarehouse(warehouseId);
        }

        model.addAttribute("items", items);
        model.addAttribute("quantities", quantities);
        model.addAttribute("itemWarehouses", itemWarehouses);
        model.addAttribute("itemWarehouseIds", itemWarehouseIds);
        model.addAttribute("warehouses", warehouseService.getAllWarehouses());
        model.addAttribute("categories", categoryService.getAllCategoryDTOs());
        model.addAttribute("loggedInWarehouseId", employee.getWarehouseID());

        return "/inventory/items";
    }

}
