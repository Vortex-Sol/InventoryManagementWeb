package vortex.imwp.services;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vortex.imwp.models.Country;
import vortex.imwp.models.Settings;
import vortex.imwp.models.TaxRate;
import vortex.imwp.repositories.SettingsChangeAuditRepository;
import vortex.imwp.repositories.SettingsRepository;
import vortex.imwp.repositories.TaxRateRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TaxRateService {
    
    @PersistenceContext
    private EntityManager entityManager;
    private final TaxRateRepository taxRateRepository;
    private final SettingsRepository settingsRepository;
    private final SettingsChangeAuditRepository settingsChangeAuditRepository;

    public TaxRateService(TaxRateRepository taxRateRepository, SettingsRepository settingsRepository
    , SettingsChangeAuditRepository settingsChangeAuditRepository) {
        this.taxRateRepository = taxRateRepository;
        this.settingsRepository = settingsRepository;
        this.settingsChangeAuditRepository = settingsChangeAuditRepository;
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

    public Double getBrutto(vortex.imwp.models.Item item, vortex.imwp.models.Warehouse warehouse) {
        vortex.imwp.models.Category category = item.getCategory();
        Double netto = item.getPrice();
        return netto * (1 + (warehouse.getSettings().getTaxRate().getRateByCategory(category))/100);
    }

    public void editVATRate(vortex.imwp.models.Category category, String countryName, Double newVATRate) {
        Country.Name country = Country.fromString(countryName);
        TaxRate taxRate = taxRateRepository.findByCountry(country);
        taxRate.editRateByCategory(category, newVATRate);
        taxRateRepository.save(taxRate);
    }

    public void removeVATRate(vortex.imwp.models.Category category, String countryName) {
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

    public List<TaxRate> findAll() {
        return (List<TaxRate>) taxRateRepository.findAll();
    }

    public Optional<TaxRate> findById(Long id) {
        return taxRateRepository.findById(id);
    }

    public Optional<TaxRate> findByCountry(Country.Name country) {
        return Optional.ofNullable(taxRateRepository.findByCountry(country));
    }

    @Transactional
    public TaxRate saveOrUpdateByCountry(TaxRate form) {
        Country.Name country = form.getCountry();
        TaxRate existing = taxRateRepository.findByCountry(country);
        if (existing == null) {
            existing = new TaxRate();
            existing.setCountry(country);
        }
        existing.setStandardRate(form.getStandardRate());
        existing.setReducedRate(form.getReducedRate());
        existing.setSuperReducedRate(form.getSuperReducedRate());
        existing.setNoneRate(form.getNoneRate());
        existing.setOtherRate(form.getOtherRate());
        return taxRateRepository.save(existing);
    }

    @Transactional
    public void hardDeleteTaxRateAndDependents(Country.Name country) {
        TaxRate tr = taxRateRepository.findByCountry(country);
        if (tr == null) {
            System.err.println("Tax rate " + country + " not found");
            return;
        }
        taxRateRepository.delete(tr);
    }

    @Transactional
    public TaxRate createNew(Country.Name country,
                             Double std, Double red, Double superRed, Double none, Double other) {
        TaxRate tr = taxRateRepository.findByCountry(country);
        if (tr == null) {
            tr = new TaxRate();
            tr.setCountry(country);
        }
        tr.setStandardRate(std);
        tr.setReducedRate(red);
        tr.setSuperReducedRate(superRed);
        tr.setNoneRate(none);
        tr.setOtherRate(other);
        return taxRateRepository.save(tr);
    }

    public Country.Name[] filterCountries(){
        Country.Name[] countries = Country.Name.values();
        for(int i = 0, counter = 0; i < countries.length && counter == 0; ++i){
            if (countries[i].toString().equalsIgnoreCase("NONE")) {
                countries[i] = null;
                ++counter;
            }
        }

        Country.Name[] filteredCountries = new Country.Name[countries.length - 1];
        for(int i = 0, k = 0; i < countries.length; ++i){
            if (countries[i] != null) {
                filteredCountries[k] = countries[i];
                ++k;
            }
        }

        return filteredCountries;
    }
}
