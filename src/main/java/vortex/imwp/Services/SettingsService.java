package vortex.imwp.Services;

import org.springframework.stereotype.Service;
import vortex.imwp.DTOs.SettingsDTO;
import vortex.imwp.Models.Employee;
import vortex.imwp.Models.Settings;
import vortex.imwp.Repositories.SettingsRepository;

import java.util.Optional;

@Service
public class SettingsService {

    private final SettingsRepository settingsRepository;

    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public boolean checkSettings(Long settingsId) {
        Optional<Settings> checkSettings = settingsRepository.findById(settingsId);
        return checkSettings.isPresent();
    }

    public Optional<Settings> getSettingsById(Long settingsId){
        return settingsRepository.findById(settingsId);
    }

    public Settings getSettingsByMangerId(Employee managerId){
        return settingsRepository.findByManagerId(managerId);
    }

    public void updateSettings(Settings settings) {
        Settings set = settingsRepository.save(settings);
        System.out.println(set);
    }




}
