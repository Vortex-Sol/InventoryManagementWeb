package vortex.imwp.mappers;

import vortex.imwp.dtos.SaleItemIdDTO;
import vortex.imwp.models.SaleItemID;

public class SaleItemIdDTOMapper {
    public static SaleItemIdDTO map(SaleItemID saleItemID) {
        return new SaleItemIdDTO(
                saleItemID.getSaleId(),
                saleItemID.getItemId()
        );
    }

    public static SaleItemID map(SaleItemIdDTO saleItemIdDTO) {
        return new SaleItemID(
                saleItemIdDTO.getSaleId(),
                saleItemIdDTO.getItemId()
        );
    }
}
