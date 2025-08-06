package vortex.imwp.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vortex.imwp.Models.SettingsChangeLog;
import vortex.imwp.Models.SettingsChangeLogID;

public interface SettingsChangeLogRepository extends JpaRepository<SettingsChangeLog, SettingsChangeLogID> {
}
