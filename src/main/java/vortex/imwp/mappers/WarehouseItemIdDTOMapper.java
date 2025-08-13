package vortex.imwp.mappers;

import vortex.imwp.dtos.WarehouseItemIdDTO;
import vortex.imwp.models.WarehouseItemID;

public class WarehouseItemIdDTOMapper {
    public static WarehouseItemIdDTO map(WarehouseItemID warehouseItemID) {
        return new WarehouseItemIdDTO(
                warehouseItemID.getWarehouseId(),
                warehouseItemID.getItemId()
        );
    }

    public static WarehouseItemID map(WarehouseItemIdDTO warehouseItemIdDTO) {
        return new WarehouseItemID(
                warehouseItemIdDTO.getWarehouseId(),
                warehouseItemIdDTO.getItemId()
        );
    }
}
