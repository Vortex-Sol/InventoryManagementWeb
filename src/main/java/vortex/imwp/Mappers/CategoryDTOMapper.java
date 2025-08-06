package vortex.imwp.Mappers;

import vortex.imwp.DTOs.CategoryDTO;
import vortex.imwp.Models.Category;
import java.util.stream.Collectors;

public class CategoryDTOMapper {
    public static CategoryDTO map(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getItems()
                        .stream()
                        .map(ItemDTOMapper::map)
                        .collect(Collectors.toSet())
        );
    }

    public static Category map(CategoryDTO dto) {
        return new Category(
                dto.getName(),
                dto.getItems()
                        .stream()
                        .map(ItemDTOMapper::map)
                        .collect(Collectors.toSet())
        );
    }
}
