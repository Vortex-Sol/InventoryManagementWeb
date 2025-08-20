package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.models.Country;
import vortex.imwp.models.TaxRate;

@Repository
public interface TaxRateRepository extends JpaRepository<TaxRate, Integer> {
    TaxRate findByCountry(Country.Name country);
}
