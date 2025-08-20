package vortex.imwp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vortex.imwp.dtos.SettingsChangeLogDTO;
import vortex.imwp.mappers.SettingsChangeLogDTOMapper;
import vortex.imwp.models.Employee;
import vortex.imwp.models.LoginAudit;
import vortex.imwp.models.SettingsChangeLog;
import vortex.imwp.repositories.SettingsChangeLogRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SettingsChangeLogService {
	private final SettingsChangeLogRepository settingsChangeLogRepository;

	public SettingsChangeLogService(SettingsChangeLogRepository settingsChangeLogRepository) {
		this.settingsChangeLogRepository = settingsChangeLogRepository;
	}

	public List<SettingsChangeLogDTO> getAllLogs() {
		return settingsChangeLogRepository.findAllByOrderByChangedAtDesc()
				.stream().map(SettingsChangeLogDTOMapper::map).toList();
	}

	public Page<SettingsChangeLogDTO> getLogsPage(int page, int size) {
		return settingsChangeLogRepository.findAllByOrderByChangedAtDesc(PageRequest.of(page, size))
				.map(SettingsChangeLogDTOMapper::map);
	}

	public List<SettingsChangeLog> getSettingsChangeLogsByEmployee(Employee employee) {
		return settingsChangeLogRepository.findByAdminId(employee.getId());
	}

	public List<SettingsChangeLog> getSettingsChangeLogsByEmployeeAndDate(Employee employee, LocalDate date) {
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay(); // exclusive
		return settingsChangeLogRepository.findByAdminIdAndChangedAtBetweenOrderByChangedAtDesc(
				employee.getId(), start, end);
	}

	public List<SettingsChangeLog> getSettingsChangeLogsByEmployeeAndPeriod(Employee employee, LocalDateTime start, LocalDateTime end) {
		return settingsChangeLogRepository.findByAdminIdAndChangedAtBetweenOrderByChangedAtDesc(
				employee.getId(), start, end);
	}
}
