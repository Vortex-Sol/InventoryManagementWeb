package vortex.imwp.mappers;

import vortex.imwp.dtos.SettingsChangeLogIdDTO;
import vortex.imwp.models.SettingsChangeAuditID;

public class SettingsChangeLogIdDTOMapper {
    public static SettingsChangeLogIdDTO map(SettingsChangeAuditID settings) {
        return new SettingsChangeLogIdDTO(
                settings.getSettingID(),
                settings.getWarehouseID(),
                settings.getAdminID()
        );
    }

    public static SettingsChangeAuditID map(SettingsChangeLogIdDTO dto){
        return new SettingsChangeAuditID(
                dto.getSettingID(),
                dto.getWarehouseID(),
                dto.getAdminID()
        );
    }
}
