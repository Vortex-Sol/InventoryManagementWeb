package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vortex.imwp.models.SettingsChangeLog;
import vortex.imwp.models.SettingsChangeLogID;

public interface SettingsChangeLogRepository extends JpaRepository<SettingsChangeLog, SettingsChangeLogID> {
}
