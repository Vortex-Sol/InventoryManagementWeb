package vortex.imwp.mappers;

import vortex.imwp.dtos.SettingsChangeAuditDTO;
import vortex.imwp.models.SettingsChangeAudit;

public class SettingsChangeAuditDTOMapper {
    public static SettingsChangeAuditDTO map(SettingsChangeAudit settings) {
        return new SettingsChangeAuditDTO(
            settings.getId(),
            settings.getSettingId(),
            settings.getWarehouseId(),
            settings.getAdminId(),
            settings.getChangedAt(),
            settings.getSettingsChanged()
        );
    }

    public static SettingsChangeAudit map(SettingsChangeAuditDTO dto) {
        return new SettingsChangeAudit(
                dto.getSettingId(),
                dto.getWarehouseId(),
                dto.getAdminId(),
                dto.getChangedAt(),
                dto.getSettingsChanged()
        );
    }
}
