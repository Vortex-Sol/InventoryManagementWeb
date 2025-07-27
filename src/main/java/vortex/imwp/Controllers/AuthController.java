package vortex.imwp.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vortex.imwp.DTOs.EmployeeDTO;
import vortex.imwp.Services.EmployeeService;
import vortex.imwp.Services.PasswordValidatorService;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final PasswordValidatorService passwordValidatorService;
    private final EmployeeService employeeService;

    public AuthController(PasswordValidatorService passwordValidatorService, EmployeeService employeeService) {
        this.passwordValidatorService = passwordValidatorService;
        this.employeeService = employeeService;
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new EmployeeDTO());
        return "auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new EmployeeDTO());
        return "admin/register";
    }

    @PostMapping("/register")
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
