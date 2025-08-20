package vortex.imwp.services;

import org.springframework.stereotype.Service;
import vortex.imwp.models.*;
import vortex.imwp.repositories.TaxRateRepository;

@Service
public class TaxRateService {

    private final TaxRateRepository taxRateRepository;

    public TaxRateService(TaxRateRepository taxRateRepository) {
        this.taxRateRepository = taxRateRepository;
    }

    public TaxRate getTaxRateByCountry(String country) {
        Country.Name countryName = Country.fromString(country);
        return taxRateRepository.findByCountry(countryName);
    }

    public boolean addTaxRate(TaxRate taxRate) {
        if (!checkTaxRateByCountry(taxRate.getCountry().name())){
            taxRateRepository.save(taxRate);
            return true;
        }
        return false;
    }

    public boolean removeTaxRate(TaxRate taxRate) {
        try {
            taxRateRepository.delete(taxRate);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Double getBrutto(Item item, Warehouse warehouse) {
        Category category = item.getCategory();
        Double netto = item.getPrice();

       return netto * (1 + (warehouse.getSettings().getTaxRate().getRateByCategory(category))/100);
    }

    public void editVATRate(Category category, String countryName, Double newVATRate) {
        Country.Name country = Country.fromString(countryName);
        TaxRate taxRate = taxRateRepository.findByCountry(country);
        taxRate.editRateByCategory(category, newVATRate);
        taxRateRepository.save(taxRate);
    }

    public void removeVATRate(Category category, String countryName) {
        Country.Name country = Country.fromString(countryName);
        TaxRate taxRate = taxRateRepository.findByCountry(country);
        taxRate.removeRateByCategory(category);
        taxRateRepository.save(taxRate);
    }

    public boolean checkTaxRateByCountry(String countryName) {
        Country.Name country = Country.fromString(countryName);
        TaxRate taxRate = taxRateRepository.findByCountry(country);
        return taxRate != null;
    }
}
