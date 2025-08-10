package vortex.imwp.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vortex.imwp.DTOs.EmployeeDTO;
import vortex.imwp.DTOs.SettingsDTO;
import vortex.imwp.Mappers.EmployeeDTOMapper;
import vortex.imwp.Mappers.SettingsDTOMapper;
import vortex.imwp.Models.Employee;
import vortex.imwp.Models.Response;
import vortex.imwp.Models.Settings;
import vortex.imwp.Repositories.SettingsRepository;
import vortex.imwp.Services.EmployeeService;
import vortex.imwp.Services.SettingsService;

import javax.swing.text.html.Option;
import java.beans.PropertyEditorSupport;
import java.sql.Time;
import java.util.Optional;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    private final SettingsService settingsService;
    private final EmployeeService employeeService;

    public SettingsController(SettingsService settingsService, EmployeeService employeeService) {
        this.settingsService = settingsService;
        this.employeeService = employeeService;
    }

    @GetMapping()
    public String getSettings(
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes) {
        try{
            Employee manager = employeeService.getEmployeeByUsername(authentication);

            Settings settings = settingsService.getSettingsByMangerId(manager);

            System.out.println(settings);
            System.out.println(settings.getManagerId());

            SettingsDTO settingsDto = SettingsDTOMapper.map(settings);
            System.out.println(settingsDto);
            model.addAttribute("settingsDto", settingsDto);
            return "settings";
        } catch (Exception e){
            redirectAttributes.addFlashAttribute("message", "Settings not found");
            return "redirect:/api/admin";
        }


    }

    @PostMapping
    public String changeSettings(@ModelAttribute("settingsDto") SettingsDTO settingsDto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            // при ошибках валидации вернём форму (модель уже содержит settingsDto и ошибки)
            return "settings";
        }
        System.out.println(settingsDto);
        settingsService.updateSettings(SettingsDTOMapper.map(settingsDto));
        redirectAttributes.addFlashAttribute("message", "Saved");
        return "redirect:/settings";
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
