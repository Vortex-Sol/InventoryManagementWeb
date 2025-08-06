package vortex.imwp.Mappers;

import vortex.imwp.DTOs.SettingsChangeLogDTO;
import vortex.imwp.Models.SettingsChangeLog;

public class SettingsChangeLogDTOMapper {
    public static SettingsChangeLogDTO map(SettingsChangeLog settings) {
        return new SettingsChangeLogDTO(
                SettingsChangeLogIdDTOMapper.map(settings.getId()),
                settings.getChangedAt(),
                settings.getSettingsChanged()
        );
    }

    public static SettingsChangeLog map(SettingsChangeLogDTO dto) {
        return new SettingsChangeLog(
                SettingsChangeLogIdDTOMapper.map(dto.getId()),
                dto.getChangedAt(),
                dto.getSettingsChanged()
        );
    }
}
