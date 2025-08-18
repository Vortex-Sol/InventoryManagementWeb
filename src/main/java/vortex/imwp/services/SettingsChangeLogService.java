package vortex.imwp.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import vortex.imwp.dtos.SettingsChangeLogDTO;
import vortex.imwp.mappers.SettingsChangeLogDTOMapper;
import vortex.imwp.repositories.SettingsChangeLogRepository;

import java.util.List;

@Service
public class SettingsChangeLogService {
	private final SettingsChangeLogRepository repo;

	public SettingsChangeLogService(SettingsChangeLogRepository repo) {
		this.repo = repo;
	}

	public List<SettingsChangeLogDTO> getAllLogs() {
		return repo.findAllByOrderByChangedAtDesc()
				.stream().map(SettingsChangeLogDTOMapper::map).toList();
	}

	public Page<SettingsChangeLogDTO> getLogsPage(int page, int size) {
		return repo.findAllByOrderByChangedAtDesc(PageRequest.of(page, size))
				.map(SettingsChangeLogDTOMapper::map);
	}
}
