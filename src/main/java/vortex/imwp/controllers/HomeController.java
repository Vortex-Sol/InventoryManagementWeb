package vortex.imwp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vortex.imwp.mappers.WarehouseDTOMapper;
import vortex.imwp.models.Employee;
import vortex.imwp.models.Warehouse;
import vortex.imwp.services.EmployeeService;
import vortex.imwp.services.WarehouseService;

import java.util.Optional;

@Controller
@RequestMapping("/api/home")
public class HomeController {
    private final EmployeeService employeeService;
    private final WarehouseService warehouseService;

    public HomeController(EmployeeService employeeService,  WarehouseService warehouseService) {
        this.employeeService = employeeService;
        this.warehouseService = warehouseService;
    }

    @GetMapping
    public String home(Authentication auth) {
        Employee employee = employeeService.getEmployeeByAuthentication(auth);
        if (employee != null && employee.getJobs() != null) {
            if (employee.getJobs().stream().anyMatch(job -> "ADMIN".equals(job.getName()))) return "redirect:/api/admin";
            else if(employee.getJobs().stream().anyMatch(job -> "MANAGER".equals(job.getName()))) return "redirect:/api/manager";
            else if(employee.getJobs().stream().anyMatch(job -> "SALESMAN".equals(job.getName()))) return "redirect:/api/salesman";
            else if(employee.getJobs().stream().anyMatch(job -> "STOCKER".equals(job.getName()))) return "redirect:/api/warehouse";
            else if(employee.getJobs().stream().anyMatch(job -> "SUPERADMIN".equals(job.getName()))) return "redirect:/api/super";
        }
        return "/error";
    }

    @GetMapping("/contact")
    public String contact(Authentication auth, Model model) {
        Optional<Warehouse> warehouse = warehouseService.getWarehouseById(employeeService.getEmployeeByAuthentication(auth).getWarehouseID());
        if(warehouse.isPresent()) {
            model.addAttribute("warehouse", WarehouseDTOMapper.map(warehouse.get()));
        }
        else{
            model.addAttribute("error", "Warehouse not found");
        }
        return "contact-information";
    }
}
