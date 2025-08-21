package vortex.imwp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vortex.imwp.models.SettingsChangeAudit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface SettingsChangeAuditRepository extends JpaRepository<SettingsChangeAudit, Long> {

	List<SettingsChangeAudit> findAllByOrderByChangedAtDesc();
	Page<SettingsChangeAudit> findAllByOrderByChangedAtDesc(Pageable pageable);

	List<SettingsChangeAudit> findByAdminIdAndChangedAtBetweenOrderByChangedAtDesc(
			Long adminId, LocalDateTime from, LocalDateTime to);
	Page<SettingsChangeAudit> findByAdminIdAndChangedAtBetweenOrderByChangedAtDesc(
			Long adminId, LocalDateTime from, LocalDateTime to, Pageable pageable);

	List<SettingsChangeAudit> findByAdminId(Long adminId);
	Page<SettingsChangeAudit> findByAdminId(Long adminId, Pageable pageable);


	Page<SettingsChangeAudit> findByWarehouseIdOrderByChangedAtDesc(Long warehouseId, Pageable pageable);

	Page<SettingsChangeAudit> findBySettingIdOrderByChangedAtDesc(Long settingId, Pageable pageable);

	@Modifying
	@Query("DELETE FROM SettingsChangeAudit scl WHERE scl.settingId IN :settingIds")
	void deleteBySettingIds(@Param("settingIds") Collection<Long> settingIds);
	void deleteByWarehouseId(Long id);
}
