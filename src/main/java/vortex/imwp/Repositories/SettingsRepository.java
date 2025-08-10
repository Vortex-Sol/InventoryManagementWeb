package vortex.imwp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vortex.imwp.Models.Employee;
import vortex.imwp.Models.Settings;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
    Settings findByManagerId(Employee managerId);
}
