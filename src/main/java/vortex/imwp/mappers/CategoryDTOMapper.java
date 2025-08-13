package vortex.imwp.mappers;

import vortex.imwp.dtos.CategoryDTO;
import vortex.imwp.models.Category;

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