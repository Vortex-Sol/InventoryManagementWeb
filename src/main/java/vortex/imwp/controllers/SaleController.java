package vortex.imwp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vortex.imwp.models.Sale;
import vortex.imwp.services.SaleService;

@RestController
@RequestMapping("/api/sales")
public class SaleController {
    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping
    @PreAuthorize("hasRole('SALESMAN')")
    public Sale createSale(@AuthenticationPrincipal UserDetails userDetails) {
        return saleService.createSale(userDetails.getUsername());
    }

}
