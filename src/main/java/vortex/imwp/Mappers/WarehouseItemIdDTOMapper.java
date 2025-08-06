package vortex.imwp.Mappers;

import vortex.imwp.DTOs.WarehouseItemIdDTO;
import vortex.imwp.Models.WarehouseItemID;

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
