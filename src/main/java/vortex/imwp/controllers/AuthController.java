package vortex.imwp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vortex.imwp.dtos.EmployeeDTO;
import vortex.imwp.services.EmployeeService;


@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("user", new EmployeeDTO());
        return "auth/login";
    }
}
