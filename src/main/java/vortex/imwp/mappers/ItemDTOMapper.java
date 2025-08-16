package vortex.imwp.mappers;

import vortex.imwp.dtos.ItemDTO;
import vortex.imwp.models.Item;

public class ItemDTOMapper {
    public static ItemDTO map(Item item) {
        return new ItemDTO(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getBarcode(),
                CategoryDTOMapper.map(item.getCategory())
        );
    }

    public static Item map(ItemDTO dto) {
        Item entity = new Item(
                dto.getId(),
                dto.getName(),
                dto.getDescription(),
                dto.getPrice(),
                dto.getBarcode()
        );
        if (dto.getCategory() != null) {
            entity.setCategory(CategoryDTOMapper.map(dto.getCategory()));
        }
        return entity;

    }
}
