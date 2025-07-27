package vortex.imwp.Mappers;

import vortex.imwp.DTOs.WarehouseDTO;
import vortex.imwp.Models.Warehouse;

public class WarehouseDTOMapper {
    public static WarehouseDTO map(Warehouse warehouse) {
        return new WarehouseDTO(warehouse.getAddress());
    }
}
