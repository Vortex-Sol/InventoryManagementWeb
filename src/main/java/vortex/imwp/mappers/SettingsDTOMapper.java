package vortex.imwp.mappers;

import vortex.imwp.dtos.SettingsDTO;
import vortex.imwp.models.Settings;

public class SettingsDTOMapper {
    public static SettingsDTO map(Settings settings) {
        return new SettingsDTO(
                settings.getId(),
                WarehouseDTOMapper.map(settings.getWarehouse()),
                settings.getAlertWhenStockIsLow(),
                settings.getAutoGenerateReport(),
                settings.getAutoGenerateReportTime(),
                settings.getNotifyMinimumCashDiscrepancy(),
                settings.getDestroyRefundDataAfterNDays(),
                settings.getCashCountStartTime(),
                settings.getCashCountEndTime(),
                settings.getAutoGenerateInventoryReportTime()
        );
    }

    public static Settings map(SettingsDTO dto) {
        return new Settings(
                WarehouseDTOMapper.map(dto.getWarehouse()),
                dto.getAlertWhenStockIsLow(),
                dto.getAutoGenerateReport(),
                dto.getAutoGenerateReportTime(),
                dto.getNotifyMinimumCashDiscrepancy(),
                dto.getDestroyRefundDataAfterNDays(),
                dto.getCashCountStartTime(),
                dto.getCashCountEndTime(),
                dto.getAutoGenerateInventoryReportTime()
        );
    }
}
