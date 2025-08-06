package vortex.imwp.Mappers;

import vortex.imwp.DTOs.CategoryDTO;
import vortex.imwp.Models.Category;
import java.util.stream.Collectors;

public class CategoryDTOMapper {
    public static CategoryDTO map(Category category) {
        return new CategoryDTO(
                category.getId(),
                category.getName()
        );
    }

    public static Category map(CategoryDTO dto) {
        return new Category(
                dto.getName()

        );
    }
}