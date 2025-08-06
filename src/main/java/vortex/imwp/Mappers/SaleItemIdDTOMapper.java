package vortex.imwp.Mappers;

import vortex.imwp.DTOs.SaleItemIdDTO;
import vortex.imwp.Models.SaleItem;
import vortex.imwp.Models.SaleItemID;

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
