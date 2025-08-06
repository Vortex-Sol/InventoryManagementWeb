package vortex.imwp.Mappers;

import vortex.imwp.DTOs.WarehouseDTO;
import vortex.imwp.Models.Warehouse;

public class WarehouseDTOMapper {
    public static WarehouseDTO map(Warehouse warehouse) {
        return new WarehouseDTO(
                warehouse.getId(),
                warehouse.getAddress()
        );
    }
    public static Warehouse map(WarehouseDTO dto) {
        return new Warehouse(
                dto.getAddress()
        );
    }
}
