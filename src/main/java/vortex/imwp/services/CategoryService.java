package vortex.imwp.services;

import org.springframework.stereotype.Service;
import vortex.imwp.dtos.CategoryDTO;
import vortex.imwp.mappers.CategoryDTOMapper;
import vortex.imwp.models.Category;
import vortex.imwp.repositories.CategoryRepository;

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

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public Optional<CategoryDTO> getCategoryDTOById(Long id) {
		return categoryRepository.findById(id)
				.map(CategoryDTOMapper::map);
	}
	public Optional<Category> getCategoryById(Long id) {
		return categoryRepository.findById(id);
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
