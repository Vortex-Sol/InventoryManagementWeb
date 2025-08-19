package vortex.imwp.controllers;


import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vortex.imwp.services.EmployeeService;
import vortex.imwp.services.PasswordValidatorService;
import vortex.imwp.dtos.EmployeeDTO;

import java.util.List;

@Controller
@RequestMapping("/api/admin")
public class AdminController {
    private final PasswordValidatorService passwordValidatorService;
    private final EmployeeService employeeService;

    public AdminController(PasswordValidatorService passwordValidatorService, EmployeeService employeeService) {
        this.passwordValidatorService = passwordValidatorService;
        this.employeeService = employeeService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
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
    public String registerEmployee(@ModelAttribute("user") EmployeeDTO employee, Model model) {
        if(!employee.getPassword().equals(employee.getConfirmPassword())) {
            model.addAttribute("error", "Passwords do not match");
            return "admin/register";
        }

        if(!passwordValidatorService.validatePassword(employee.getPassword())) {
            model.addAttribute("error", "Password must be: 8 Characters, has a digit, at least 1 capital letter and 1 lower letter");
            return "admin/register";
        }

        employeeService.registerEmployee(employee);
        return "redirect:/inventory/home";
    }
}
