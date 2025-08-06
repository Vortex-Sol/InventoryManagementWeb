package vortex.imwp.Mappers;

import vortex.imwp.DTOs.SettingsChangeLogIdDTO;
import vortex.imwp.Models.SettingsChangeLogID;

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
