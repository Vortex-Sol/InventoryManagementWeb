package vortex.imwp.Mappers;

import vortex.imwp.DTOs.ItemDTO;
import vortex.imwp.Models.Item;

public class ItemDTOMapper {
    public static ItemDTO map(Item item) {
        ItemDTO dto = new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice()
        );

        return dto;
    }

    public static Item map(ItemDTO dto) {
        return new Item(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getPrice()
        );
    }
}
