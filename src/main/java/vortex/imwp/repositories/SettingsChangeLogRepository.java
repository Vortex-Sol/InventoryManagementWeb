package vortex.imwp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import vortex.imwp.models.SettingsChangeLog;

import java.time.LocalDateTime;
import java.util.List;

public interface SettingsChangeLogRepository extends JpaRepository<SettingsChangeLog, Long> {

	List<SettingsChangeLog> findAllByOrderByChangedAtDesc();
	Page<SettingsChangeLog> findAllByOrderByChangedAtDesc(Pageable pageable);

	List<SettingsChangeLog> findByAdminIdAndChangedAtBetweenOrderByChangedAtDesc(
			Long adminId, LocalDateTime from, LocalDateTime to);
	Page<SettingsChangeLog> findByAdminIdAndChangedAtBetweenOrderByChangedAtDesc(
			Long adminId, LocalDateTime from, LocalDateTime to, Pageable pageable);

	List<SettingsChangeLog> findByAdminId(Long adminId);
	Page<SettingsChangeLog> findByAdminId(Long adminId, Pageable pageable);


	Page<SettingsChangeLog> findByWarehouseIdOrderByChangedAtDesc(Long warehouseId, Pageable pageable);

	Page<SettingsChangeLog> findBySettingIdOrderByChangedAtDesc(Long settingId, Pageable pageable);


}
