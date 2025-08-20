package vortex.imwp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import vortex.imwp.models.Employee;
import vortex.imwp.services.EmployeeService;

@Controller
@RequestMapping("/api/home")
public class HomeController {
    private final EmployeeService employeeService;

    public HomeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
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
}
