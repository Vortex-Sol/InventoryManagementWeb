package vortex.imwp.mappers;

import vortex.imwp.dtos.WarehouseDTO;
import vortex.imwp.models.Warehouse;

public class WarehouseDTOMapper {
    public static WarehouseDTO map(Warehouse warehouse) {
        return new WarehouseDTO(
                warehouse.getId(),
                warehouse.getPhone(),
                warehouse.getEmail(),
                warehouse.getAddress()
        );
    }
    public static Warehouse map(WarehouseDTO dto) {
        return new Warehouse(
                dto.getPhone(),
                dto.getEmail(),
                dto.getAddress()
        );
    }
}
