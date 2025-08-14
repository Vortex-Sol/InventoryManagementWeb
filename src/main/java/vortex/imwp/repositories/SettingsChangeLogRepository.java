package vortex.imwp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vortex.imwp.models.SettingsChangeAudit;
import vortex.imwp.models.SettingsChangeAuditID;

public interface SettingsChangeLogRepository extends JpaRepository<SettingsChangeAudit, SettingsChangeAuditID> {
}
