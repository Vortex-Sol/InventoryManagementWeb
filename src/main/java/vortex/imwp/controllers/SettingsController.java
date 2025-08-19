package vortex.imwp.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vortex.imwp.dtos.SettingsDTO;
import vortex.imwp.mappers.SettingsDTOMapper;
import vortex.imwp.models.Employee;
import vortex.imwp.models.Settings;
import vortex.imwp.services.EmployeeService;
import vortex.imwp.services.SettingsService;

import java.beans.PropertyEditorSupport;
import java.sql.Time;
import java.util.Set;

@Controller
@RequestMapping("/api/settings")
public class SettingsController {

    private final SettingsService settingsService;
    private final EmployeeService employeeService;

    public SettingsController(SettingsService settingsService, EmployeeService employeeService) {
        this.settingsService = settingsService;
        this.employeeService = employeeService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public String getSettings(
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {
        try{
            Employee manager = employeeService.getEmployeeByAuthentication(authentication);
            Set<String> allowed = Set.of("ADMIN", "SUPERADMIN");

            if (manager != null && manager.getJobs() != null &&
                    manager.getJobs().stream().anyMatch(job -> allowed.contains(job.getName()))) {
                Settings settings = settingsService.getSettingsByMangerId(manager);
                SettingsDTO settingsDto = SettingsDTOMapper.map(settings);
                model.addAttribute("settingsDto", settingsDto);
                return "/admin/settings";
            }

            return "redirect:/api/home";

        } catch (Exception e){
            redirectAttributes.addFlashAttribute("message", "Settings not found");
            return "redirect:/api/home";
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    public String changeSettings(@ModelAttribute("settingsDto") SettingsDTO settingsDto,
                                 Authentication authentication,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {

        Employee manager = employeeService.getEmployeeByAuthentication(authentication);
        Set<String> allowed = Set.of("ADMIN", "SUPERADMIN");
        if (manager != null && manager.getJobs() != null &&
                manager.getJobs().stream().anyMatch(job -> allowed.contains(job.getName()))){

            if (bindingResult.hasErrors()) {
                return "/admin/settings";
            }
            System.out.println(settingsDto);
            settingsService.updateSettings(settingsDto, authentication);
            redirectAttributes.addFlashAttribute("message", "Saved");
            return "redirect:/api/settings";
        }
        return "redirect:/api/home";
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Time.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                    return;
                }
                String t = text.trim();
                if (t.chars().filter(ch -> ch == ':').count() == 1) {
                    setValue(Time.valueOf(t + ":00"));
                } else {
                    setValue(Time.valueOf(t));
                }
            }
            @Override
            public String getAsText() {
                Time time = (Time) getValue();
                return time == null ? "" : time.toString();
            }
        });
    }
}
