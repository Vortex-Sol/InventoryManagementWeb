package vortex.imwp.mappers;

import vortex.imwp.dtos.SaleItemDTO;
import vortex.imwp.models.SaleItem;

public class SaleItemDTOMapper {
    public static SaleItemDTO map(SaleItem saleItem) {
        return new SaleItemDTO(
                SaleItemIdDTOMapper.map(saleItem.getId()),
                SaleDTOMapper.map(saleItem.getSale()),
                ItemDTOMapper.map((saleItem.getItem())),
                saleItem.getQuantity()
        );
    }

    public static SaleItem map(SaleItemDTO saleItemDTO) {
        return new SaleItem(
                SaleDTOMapper.map(saleItemDTO.getSale()),
                ItemDTOMapper.map(saleItemDTO.getItem()),
                saleItemDTO.getQuantity()
        );
    }
}
