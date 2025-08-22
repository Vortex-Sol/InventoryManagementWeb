package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.models.Employee;
import vortex.imwp.models.Settings;

import java.util.List;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Settings findByWarehouse_Id(Long warehouseId);
    List<Settings> findAllByTaxRate_Id(Long taxRateId);

}
