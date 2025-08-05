package vortex.imwp.Controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vortex.imwp.Models.Employee;
import vortex.imwp.Models.Sale;
import vortex.imwp.Repositories.EmployeeRepository;
import vortex.imwp.Repositories.SaleRepository;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private EmployeeRepository employeeRepository;
    private SaleRepository saleRepository;
    public SaleController(EmployeeRepository employeeRepository, SaleRepository saleRepository) {
        this.employeeRepository = employeeRepository;
        this.saleRepository = saleRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('SALESMAN')")
    public Sale createSale(@RequestParam double amount,
                           @AuthenticationPrincipal UserDetails userDetails) {

        Employee employee = employeeRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Sale sale = new Sale();
        /*sale.setAmount(amount);
        sale.setEmployee(employee);*/
        return saleRepository.save(sale);
    }
    @GetMapping("/checkout")
    public String inventoryHome() {
        return "inventory/checkout";
    }
}
