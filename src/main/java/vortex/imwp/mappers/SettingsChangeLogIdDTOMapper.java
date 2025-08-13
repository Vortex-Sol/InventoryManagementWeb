package vortex.imwp.mappers;

import vortex.imwp.dtos.SettingsChangeLogIdDTO;
import vortex.imwp.models.SettingsChangeLogID;

public class SettingsChangeLogIdDTOMapper {
    public static SettingsChangeLogIdDTO map(SettingsChangeLogID settings) {
        return new SettingsChangeLogIdDTO(
                settings.getSettingID(),
                settings.getWarehouseID(),
                settings.getAdminID()
        );
    }

    public static SettingsChangeLogID map(SettingsChangeLogIdDTO dto){
        return new SettingsChangeLogID(
                dto.getSettingID(),
                dto.getWarehouseID(),
                dto.getAdminID()
        );
    }
}
