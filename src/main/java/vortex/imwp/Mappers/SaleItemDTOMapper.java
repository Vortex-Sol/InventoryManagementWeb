package vortex.imwp.Mappers;

import vortex.imwp.DTOs.SaleItemDTO;
import vortex.imwp.Models.SaleItem;

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
