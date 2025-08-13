package vortex.imwp.mappers;

import vortex.imwp.dtos.SettingsChangeLogDTO;
import vortex.imwp.models.SettingsChangeLog;

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
