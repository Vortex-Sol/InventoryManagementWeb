package vortex.imwp.controllers;

import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vortex.imwp.dtos.WarehouseDTO;
import vortex.imwp.mappers.WarehouseDTOMapper;
import vortex.imwp.models.Warehouse;
import vortex.imwp.services.EmployeeService;
import vortex.imwp.services.PasswordValidatorService;
import vortex.imwp.dtos.EmployeeDTO;
import vortex.imwp.services.WarehouseService;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
    private final PasswordValidatorService passwordValidatorService;
    private final EmployeeService employeeService;
    private final WarehouseService warehouseService;

    public AdminController(PasswordValidatorService passwordValidatorService, EmployeeService employeeService,  WarehouseService warehouseService) {
        this.passwordValidatorService = passwordValidatorService;
        this.employeeService = employeeService;
        this.warehouseService = warehouseService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public String admin() {
        return "/admin/admin-dashboard";
    }

    @GetMapping("/user-management")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public String userManagement(Model model) {
        var users = employeeService.getAllEmployees().orElseGet(List::of);
        model.addAttribute("users", users);
        model.addAttribute("roles", List.of("ADMIN","MANAGER","STOCKER","SALESMAN"));
        return "/admin/user-management";
    }

    @Transactional
    @PostMapping("/users/promote")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public String promoteUser(@RequestParam("id") Long userId,
                              @RequestParam("role") String roleName) {
        employeeService.assignRole(userId, roleName);
        return "redirect:/api/admin/user-management";
    }

    @Transactional
    @PostMapping("/users/demote")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public String demoteUser(@RequestParam("id") Long userId,
                             @RequestParam("role") String roleName) {
        employeeService.removeRole(userId, roleName);
        return "redirect:/api/admin/user-management";
    }



    @GetMapping("/register")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public String register(Model model) {
        model.addAttribute("user", new EmployeeDTO());
        return "admin/register";
    }

    @PostMapping("/register")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public String registerEmployee(@ModelAttribute("user") EmployeeDTO employeeDTO, Model model) {
        if(!employeeDTO.getPassword().equals(employeeDTO.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "admin/register";
        }

        if(!passwordValidatorService.validatePassword(employeeDTO.getPassword())) {
            model.addAttribute("error", "Password must be: 8 Characters, has a digit, at least 1 capital letter and 1 lower letter");
            return "admin/register";
        }

        if(employeeService.getEmployeeByUsername(employeeDTO.getUsername()).isEmpty()) {
            model.addAttribute("error", "Employee by username \"" + employeeDTO.getUsername() + "\" already exists");
        }

        employeeService.registerEmployee(employeeDTO);
        return "redirect:/inventory/admin";
    }

    @GetMapping("/contact")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public String contact(Model model, Authentication authentication) {
        Optional<Warehouse> warehouse = warehouseService.getWarehouseById(employeeService.getEmployeeByAuthentication(authentication).getWarehouseID());
        if(warehouse.isPresent()) {
            System.out.println("[TESTING] Warehouse ID: "  + warehouse.get().getId());
            model.addAttribute("warehouse", WarehouseDTOMapper.map(warehouse.get()));
            return "/admin/edit-contact-information";
        }
        model.addAttribute("warehouse", new WarehouseDTO());
        return "/admin/edit-contact-information";
    }

    @PostMapping("/contact")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public String editContact(@ModelAttribute WarehouseDTO dto, Authentication authentication, Model model) {
        System.out.println("[TESTING PART 2]\n[Warehouse ID] " + dto.getId() + "\n[Warehouse address] " +  dto.getAddress() + "\n[Warehouse email] " + dto.getEmail());
        warehouseService.updateWarehouse(dto.getId(), dto);
        return "redirect:/api/home";
    }
}
