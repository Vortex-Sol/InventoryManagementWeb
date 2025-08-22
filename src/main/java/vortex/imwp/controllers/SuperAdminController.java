package vortex.imwp.controllers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import vortex.imwp.dtos.SettingsDTO;
import vortex.imwp.dtos.TaxRateDTO;
import vortex.imwp.dtos.WarehouseDTO;
import vortex.imwp.models.Country;
import vortex.imwp.models.TaxRate;
import vortex.imwp.models.Warehouse;
import vortex.imwp.services.SettingsService;
import vortex.imwp.services.SuperAdminService;
import vortex.imwp.services.TaxRateService;
import vortex.imwp.services.WarehouseService;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/api/super")
public class SuperAdminController {

    private final SuperAdminService superAdminService;
    private final WarehouseService warehouseService;
    private final TaxRateService taxRateService;
    private final SettingsService settingsService;

    public SuperAdminController(SuperAdminService superAdminService,
                                WarehouseService warehouseService,
                                TaxRateService taxRateService,
                                SettingsService settingsService) {
        this.superAdminService = superAdminService;
        this.warehouseService = warehouseService;
        this.taxRateService = taxRateService;
        this.settingsService = settingsService;
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String superAdmin() { return "super/super-dashboard"; }

    @GetMapping("/taxes")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String superAdminTaxes() { return "super/tax-rates"; }

    @ModelAttribute("countries")
    public List<Country.Name> countries() {
        return Arrays.asList(Country.Name.values());
    }

    @ModelAttribute("taxRates")
    public List<TaxRateDTO> taxRates() {
        return superAdminService.getAllTaxRatesDTO();
    }

    @ModelAttribute("warehouses")
    public List<WarehouseDTO> warehouses() {
        return superAdminService.listAllWarehousesDTO();
    }

    @GetMapping("/warehouses")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String superAdminWarehouses(Model model) {
        model.addAttribute("mode", "list");
        return "super/super-warehouse";
    }

    @GetMapping("/warehouses/create")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String createWarehousePage(Model model) {
        model.addAttribute("mode", "create");
        model.addAttribute("warehouseForm", new WarehouseDTO());
        return "super/super-warehouse";
    }

    @PostMapping("/warehouses/create")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String createWarehouse(
            @ModelAttribute("warehouseForm") WarehouseDTO w,
            @RequestParam("taxRateMode") String taxRateMode,
            @RequestParam(value = "existingTaxRateId", required = false) Long existingTaxRateId,
            @RequestParam(value = "newRateCountry", required = false) Country.Name newRateCountry,
            @RequestParam(value = "newRateStandardRate", required = false) Double newRateStandardRate,
            @RequestParam(value = "newRateReducedRate", required = false) Double newRateReducedRate,
            @RequestParam(value = "newRateSuperReducedRate", required = false) Double newRateSuperReducedRate,
            @RequestParam(value = "newRateNoneRate", required = false) Double newRateNoneRate,
            @RequestParam(value = "newRateOtherRate", required = false) Double newRateOtherRate
    ) {
        Warehouse warehouse = warehouseService.createWarehouseBasic(
                w.getPhone(), w.getEmail(), w.getAddress()
        );
        System.out.println(warehouse +"1fsa");

        TaxRate taxRate;
        if ("existing".equalsIgnoreCase(taxRateMode)) {
            taxRate = taxRateService.findById(existingTaxRateId).orElse(null);
        } else {
            taxRate = taxRateService.createNew(
                    newRateCountry, newRateStandardRate, newRateReducedRate,
                    newRateSuperReducedRate, newRateNoneRate, newRateOtherRate
            );

        }

        settingsService.createDefaultSettingsForWarehouse(warehouse, taxRate);

        return "redirect:/api/super/warehouses";
    }

    @GetMapping("/warehouses/{id}/edit")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String editWarehousePage(@PathVariable Long id, Model model) {
        WarehouseDTO w = warehouseService.getWarehouseDTOById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse not found"));

        SettingsDTO settings = settingsService.getSettingsByWarehouseDTOId(id);
        TaxRateDTO currentTax = (settings != null && settings.getId() != null)
                ? settingsService.getTaxRateDTOById(settings.getId())
                : null;

        model.addAttribute("warehouseForm", w);
        model.addAttribute("currentTaxRate", currentTax);
        model.addAttribute("mode", "edit");
        return "super/super-warehouse";
    }

    @PostMapping("/warehouses/{id}/update")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String updateWarehouse(
            @PathVariable Long id,
            @ModelAttribute("warehouseForm") WarehouseDTO w,
            @RequestParam("taxRateMode") String taxRateMode,
            @RequestParam(value = "existingTaxRateId", required = false) Long existingTaxRateId,
            @RequestParam(value = "newRateCountry", required = false) Country.Name newRateCountry,
            @RequestParam(value = "newRateStandardRate", required = false) Double newRateStandardRate,
            @RequestParam(value = "newRateReducedRate", required = false) Double newRateReducedRate,
            @RequestParam(value = "newRateSuperReducedRate", required = false) Double newRateSuperReducedRate,
            @RequestParam(value = "newRateNoneRate", required = false) Double newRateNoneRate,
            @RequestParam(value = "newRateOtherRate", required = false) Double newRateOtherRate
    ) {

        warehouseService.updateWarehouse(id, w);

        TaxRate taxRate;
        if ("existing".equalsIgnoreCase(taxRateMode)) {
            taxRate = taxRateService.findById(existingTaxRateId).orElse(null);
        } else {
            taxRate = taxRateService.createNew(
                    newRateCountry, newRateStandardRate, newRateReducedRate,
                    newRateSuperReducedRate, newRateNoneRate, newRateOtherRate
            );
        }

        warehouseService.upsertSettingsForWarehouse(id, taxRate);

        return "redirect:/api/super/warehouses";
    }

    @PostMapping("/warehouses/{id}/delete")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public String deleteWarehouse(@PathVariable Long id, Model model) {
        try {
            superAdminService.deleteWarehouse(id);
            return "redirect:/api/super/warehouses";
        } catch (DataIntegrityViolationException dive) {
            model.addAttribute("deleteError",
                    "Cannot delete warehouse " + id + " because it is referenced by other records. " +
                            "Reassign or remove those references first.");
            model.addAttribute("mode", "list");
            return "super/super-warehouse";
        } catch (Exception ex) {
            model.addAttribute("deleteError", "Failed to delete warehouse " + id + ": " + ex.getMessage());
            model.addAttribute("mode", "list");
            return "super/super-warehouse";
        }
    }

    @ModelAttribute("taxRateForm")
    public TaxRate taxRateForm() {
        return new TaxRate();
    }

}
