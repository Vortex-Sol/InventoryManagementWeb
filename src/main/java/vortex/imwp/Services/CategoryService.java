package vortex.imwp.Services;

import org.springframework.stereotype.Service;
import vortex.imwp.DTOs.CategoryDTO;
import vortex.imwp.Mappers.CategoryDTOMapper;
import vortex.imwp.Mappers.WarehouseDTOMapper;
import vortex.imwp.Models.Category;
import vortex.imwp.Repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public CategoryService(CategoryRepository categoryRepository) {
		this.categoryRepository = categoryRepository;
	}

	public List<CategoryDTO> getAllCategoryDTOs() {
		List<Category> cats = categoryRepository.findAll();
		return cats.stream()
				.map(CategoryDTOMapper::map)
				.collect(Collectors.toList());
	}

	public Optional<CategoryDTO> getCategoryDTOById(Long id) {
		return categoryRepository.findById(id)
				.map(CategoryDTOMapper::map);
	}
	public CategoryDTO createCategoryIfNotExists(CategoryDTO dto) {
		Optional<Category> existing = categoryRepository.findByNameIgnoreCase(dto.getName().trim());
		if (existing.isPresent()) {
			return CategoryDTOMapper.map(existing.get());
		} else {
			Category category = new Category();
			category.setName(dto.getName().trim());
			Category saved = categoryRepository.save(category);
			return CategoryDTOMapper.map(saved);
		}
	}


}
