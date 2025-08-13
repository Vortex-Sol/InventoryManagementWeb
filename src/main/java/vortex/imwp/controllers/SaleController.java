package vortex.imwp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vortex.imwp.dtos.EmployeeDTO;
import vortex.imwp.dtos.SaleDTO;
import vortex.imwp.mappers.EmployeeDTOMapper;
import vortex.imwp.models.Employee;
import vortex.imwp.models.Sale;
import vortex.imwp.services.EmployeeService;
import vortex.imwp.services.SaleService;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;
    private final EmployeeService employeeService;

    public SaleController(SaleService saleService, EmployeeService employeeService) {
        this.saleService = saleService;
        this.employeeService = employeeService;
    }
    @PostMapping
    @PreAuthorize("hasRole('SALESMAN')")
    public Sale createSale(@RequestParam double amount,
                           @AuthenticationPrincipal UserDetails userDetails) {

        EmployeeDTO employeeDTO = employeeService.getEmployeeByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        Employee employee = EmployeeDTOMapper.map(employeeDTO);

        SaleDTO sale = new SaleDTO();
        /*sale.setAmount(amount);
        sale.setEmployee(employee);*/
        return saleService.addSale(sale);
    }
    @PostMapping("/new")
    @PreAuthorize("hasRole('SALESMAN')")
    public Sale createSale(@AuthenticationPrincipal UserDetails userDetails) {
        return saleService.createSale(userDetails.getUsername());
    }

}
