package vortex.imwp.controllers;

import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vortex.imwp.dtos.SettingsChangeLogDTO;
import vortex.imwp.services.SettingsChangeLogService;

@Controller
@RequestMapping("/api")
public class ActivityLogController {

	private final SettingsChangeLogService logService;

	public ActivityLogController(SettingsChangeLogService logService) {
		this.logService = logService;
	}

	@GetMapping("/logs")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
	public String logs(Model model) {
		model.addAttribute("logs", logService.getAllLogs());
		return "admin/activity-log"; 
	}

	@GetMapping("/logs/paged")
	@PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
	public String logsPaged(@RequestParam(defaultValue = "0") int page,
							@RequestParam(defaultValue = "20") int size,
							Model model) {
		Page<SettingsChangeLogDTO> logsPage = logService.getLogsPage(page, size);
		model.addAttribute("logsPage", logsPage);
		model.addAttribute("logs", logsPage.getContent());
		return "admin/activity-log";
	}
}
