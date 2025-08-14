package vortex.imwp.mappers;

import vortex.imwp.dtos.SettingsChangeLogDTO;
import vortex.imwp.models.SettingsChangeAudit;

public class SettingsChangeLogDTOMapper {
    public static SettingsChangeLogDTO map(SettingsChangeAudit settings) {
        return new SettingsChangeLogDTO(
                SettingsChangeLogIdDTOMapper.map(settings.getId()),
                settings.getChangedAt(),
                settings.getSettingsChanged()
        );
    }

    public static SettingsChangeAudit map(SettingsChangeLogDTO dto) {
        return new SettingsChangeAudit(
                SettingsChangeLogIdDTOMapper.map(dto.getId()),
                dto.getChangedAt(),
                dto.getSettingsChanged()
        );
    }
}
