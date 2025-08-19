package vortex.imwp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/super")
public class SuperAdminController {

    @GetMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String superAdmin() {
        return "super/super-dashboard";
    }

    @GetMapping("/warehouses")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String superAdminWarehouses() {
        return "super/super-warehouse";
    }

    @GetMapping("/taxes")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String superAdminTaxes() {
        return "super/tax-rates";
    }
}
