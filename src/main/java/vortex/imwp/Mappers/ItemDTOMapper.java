package vortex.imwp.Mappers;

import vortex.imwp.DTOs.ItemDTO;
import vortex.imwp.Models.Item;
import java.util.stream.Collectors;

public class ItemDTOMapper {
    public static ItemDTO map(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getBarcode(),
                CategoryDTOMapper.map(item.getCategory()),
                item.getSaleItems()
                        .stream()
                        .map(SaleItemDTOMapper::map)
                        .collect(Collectors.toList())
        );
    }

    public static Item map(ItemDTO dto) {
        return new Item(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getBarcode()
        );
    }
}
