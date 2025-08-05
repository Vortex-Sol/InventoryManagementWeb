package vortex.imwp.Mappers;

import vortex.imwp.DTOs.ItemDTO;
import vortex.imwp.Models.Item;

public class ItemDTOMapper {
    public static ItemDTO map(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                0,
                item.getBarcode()
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
