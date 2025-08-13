package vortex.imwp.mappers;

import vortex.imwp.dtos.WarehouseItemDTO;
import vortex.imwp.models.WarehouseItem;

public class WarehouseItemDTOMapper {
    public static WarehouseItemDTO map(WarehouseItem warehouseItem) {
        return new WarehouseItemDTO(
                WarehouseDTOMapper.map(warehouseItem.getWarehouse()),
                ItemDTOMapper.map(warehouseItem.getItem()),
                warehouseItem.getQuantityInStock()
        );
    }

    public static WarehouseItem map(WarehouseItemDTO dto) {
        return new WarehouseItem(
                WarehouseDTOMapper.map(dto.getWarehouse()),
                ItemDTOMapper.map(dto.getItem()),
                dto.getQuantityInStock()
        );
    }
}
