package vortex.imwp.mappers;

import vortex.imwp.dtos.ItemChangeLogDTO;
import vortex.imwp.models.ItemChangeLog;

public class ItemChangeLogDTOMapper {

    public static ItemChangeLogDTO map(ItemChangeLog itemChangeLog) {
        return new ItemChangeLogDTO(
                itemChangeLog.getItemId(),
                itemChangeLog.getItemId(),
                itemChangeLog.getStockerId(),
                itemChangeLog.getWarehouseId(),
                itemChangeLog.getChangedAt(),
                itemChangeLog.getDataChanged()
        );
    }

    public static ItemChangeLog map(ItemChangeLogDTO itemChangeLogDTO) {
        return new ItemChangeLog(
                itemChangeLogDTO.getItemId(),
                itemChangeLogDTO.getStockerId(),
                itemChangeLogDTO.getWarehouseId(),
                itemChangeLogDTO.getChangedAt(),
                itemChangeLogDTO.getDataChanged()
        );
    }
}
