package vortex.imwp.mappers;

import vortex.imwp.dtos.SettingsChangeLogDTO;
import vortex.imwp.models.SettingsChangeLog;

public class SettingsChangeLogDTOMapper {
    public static SettingsChangeLogDTO map(SettingsChangeLog settings) {
        return new SettingsChangeLogDTO(
            settings.getId(),
            settings.getSettingId(),
            settings.getWarehouseId(),
            settings.getAdminId(),
            settings.getChangedAt(),
            settings.getSettingsChanged()
        );
    }

    public static SettingsChangeLog map(SettingsChangeLogDTO dto) {
        return new SettingsChangeLog(
                dto.getSettingId(),
                dto.getWarehouseId(),
                dto.getAdminId(),
                dto.getChangedAt(),
                dto.getSettingsChanged()
        );
    }
}
