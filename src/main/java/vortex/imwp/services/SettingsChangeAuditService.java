package vortex.imwp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vortex.imwp.dtos.SettingsChangeAuditDTO;
import vortex.imwp.mappers.SettingsChangeAuditDTOMapper;
import vortex.imwp.models.Employee;
import vortex.imwp.models.SettingsChangeAudit;
import vortex.imwp.repositories.SettingsChangeAuditRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SettingsChangeAuditService {
	private final SettingsChangeAuditRepository settingsChangeAuditRepository;

	public SettingsChangeAuditService(SettingsChangeAuditRepository settingsChangeAuditRepository) {
		this.settingsChangeAuditRepository = settingsChangeAuditRepository;
	}

	public List<SettingsChangeAuditDTO> getAllLogs() {
		return settingsChangeAuditRepository.findAllByOrderByChangedAtDesc()
				.stream().map(SettingsChangeAuditDTOMapper::map).toList();
	}

	public Page<SettingsChangeAuditDTO> getLogsPage(int page, int size) {
		return settingsChangeAuditRepository.findAllByOrderByChangedAtDesc(PageRequest.of(page, size))
				.map(SettingsChangeAuditDTOMapper::map);
	}

	public List<SettingsChangeAudit> getSettingsChangeLogsByEmployee(Employee employee) {
		return settingsChangeAuditRepository.findByAdminId(employee.getId());
	}

	public List<SettingsChangeAudit> getSettingsChangeLogsByEmployeeAndDate(Employee employee, LocalDate date) {
		LocalDateTime start = date.atStartOfDay();
		LocalDateTime end = date.plusDays(1).atStartOfDay(); // exclusive
		return settingsChangeAuditRepository.findByAdminIdAndChangedAtBetweenOrderByChangedAtDesc(
				employee.getId(), start, end);
	}

	public List<SettingsChangeAudit> getSettingsChangeLogsByEmployeeAndPeriod(Employee employee, LocalDateTime start, LocalDateTime end) {
		return settingsChangeAuditRepository.findByAdminIdAndChangedAtBetweenOrderByChangedAtDesc(
				employee.getId(), start, end);
	}
}
