package category.service;

import category.dto.CategoryDto;
import category.dto.CreateCategoryDto;
import category.dto.UpdateCategoryDto;
import category.model.Category;
import category.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryCrudService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryCrudService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapperService mapperService;

    // CREATE
    public CategoryDto createCategory(CreateCategoryDto createDto) {
        logger.info("[CATEGORY] Tworzenie nowej kategorii: {}", createDto.getName());

        validateCategoryCreation(createDto);

        Category parent = null;
        if (createDto.getParentId() != null) {
            parent = categoryRepository.findById(createDto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Kategoria nadrzędna nie została znaleziona: " + createDto.getParentId()));
        }

        Category category = new Category();
        category.setName(createDto.getName());
        category.setDescription(createDto.getDescription());
        category.setSlug(createDto.getSlug());
        category.setImageUrl(createDto.getImageUrl());
        category.setActive(createDto.getActive());
        category.setSortOrder(createDto.getSortOrder());
        category.setParent(parent);

        Category saved = categoryRepository.save(category);
        logger.info("[CATEGORY] Kategoria utworzona: {} (ID: {})", saved.getName(), saved.getId());

        return mapperService.convertToDto(saved);
    }

    // READ
    @Cacheable(value = "categories", key = "#id")
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategoria nie została znaleziona: " + id));
        return mapperService.convertToDto(category);
    }

    @Cacheable(value = "categories", key = "'slug-' + #slug")
    public CategoryDto getCategoryBySlug(String slug) {
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Kategoria nie została znaleziona: " + slug));
        return mapperService.convertToDto(category);
    }

    // UPDATE
    @CacheEvict(value = {"categories", "categoryList", "categoryTree"}, allEntries = true)
    public CategoryDto updateCategory(Long id, UpdateCategoryDto updateDto) {
        logger.info("[CATEGORY] Aktualizacja kategorii ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategoria nie została znaleziona: " + id));

        validateCategoryUpdate(id, updateDto, category);
        updateCategoryFromDto(category, updateDto);

        Category saved = categoryRepository.save(category);
        logger.info("[CATEGORY] Kategoria zaktualizowana: {} (ID: {})", saved.getName(), saved.getId());

        return mapperService.convertToDto(saved);
    }

    // DELETE
    @CacheEvict(value = {"categories", "categoryList", "categoryTree"}, allEntries = true)
    public void deleteCategory(Long id) {
        logger.info("[CATEGORY] Usuwanie kategorii ID: {}", id);

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Kategoria nie została znaleziona: " + id));

        if (!categoryRepository.canBeDeleted(id)) {
            throw new RuntimeException("Nie można usunąć kategorii, która ma produkty lub kategorie potomne");
        }

        categoryRepository.delete(category);
        logger.info("[CATEGORY] Kategoria usunięta: {} (ID: {})", category.getName(), id);
    }

    // VALIDATION METHODS
    private void validateCategoryCreation(CreateCategoryDto dto) {
        if (categoryRepository.existsBySlug(dto.getSlug())) {
            throw new RuntimeException("Kategoria z tym slug już istnieje: " + dto.getSlug());
        }
    }

    private void validateCategoryUpdate(Long id, UpdateCategoryDto updateDto, Category category) {
        if (updateDto.getSlug() != null &&
                !updateDto.getSlug().equals(category.getSlug()) &&
                categoryRepository.existsBySlugAndIdNot(updateDto.getSlug(), id)) {
            throw new RuntimeException("Kategoria z tym slug już istnieje: " + updateDto.getSlug());
        }

        if (updateDto.getParentId() != null) {
            if (updateDto.getParentId().equals(id)) {
                throw new RuntimeException("Kategoria nie może być swoim własnym rodzicem");
            }

            Category parent = categoryRepository.findById(updateDto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Kategoria nadrzędna nie została znaleziona: " + updateDto.getParentId()));

            if (isDescendantOf(parent, category)) {
                throw new RuntimeException("Nie można ustawić kategorii potomnej jako rodzica");
            }
        }
    }

    private void updateCategoryFromDto(Category category, UpdateCategoryDto dto) {
        if (dto.getName() != null) category.setName(dto.getName());
        if (dto.getDescription() != null) category.setDescription(dto.getDescription());
        if (dto.getSlug() != null) category.setSlug(dto.getSlug());
        if (dto.getImageUrl() != null) category.setImageUrl(dto.getImageUrl());
        if (dto.getActive() != null) category.setActive(dto.getActive());
        if (dto.getSortOrder() != null) category.setSortOrder(dto.getSortOrder());

        if (dto.getParentId() != null) {
            Category parent = categoryRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Kategoria nadrzędna nie została znaleziona: " + dto.getParentId()));
            category.setParent(parent);
        } else if (dto.getParentId() == null) {
            category.setParent(null);
        }
    }

    private boolean isDescendantOf(Category potentialAncestor, Category category) {
        Category current = potentialAncestor.getParent();
        while (current != null) {
            if (current.getId().equals(category.getId())) {
                return true;
            }
            current = current.getParent();
        }
        return false;
    }
}
