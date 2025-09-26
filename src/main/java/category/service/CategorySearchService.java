package category.service;

import category.dto.CategoryDto;
import category.dto.CategoryListDto;
import category.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategorySearchService {

    private static final Logger logger = LoggerFactory.getLogger(CategorySearchService.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapperService mapperService;

    @Cacheable(value = "categoryList", key = "'active'")
    public List<CategoryDto> getAllActiveCategories() {
        logger.debug("[CATEGORY] Pobieranie wszystkich aktywnych kategorii");
        return categoryRepository.findByActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(mapperService::convertToDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "categoryTree", key = "'root'")
    public List<CategoryDto> getRootCategories() {
        logger.debug("[CATEGORY] Pobieranie kategorii głównych");
        return categoryRepository.findByParentIsNullAndActiveTrue()
                .stream()
                .map(mapperService::convertToDtoWithChildren)
                .collect(Collectors.toList());
    }

    public List<CategoryDto> getChildCategories(Long parentId) {
        logger.debug("[CATEGORY] Pobieranie kategorii potomnych dla ID: {}", parentId);
        return categoryRepository.findActiveChildrenByParentId(parentId)
                .stream()
                .map(mapperService::convertToDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "categoryList", key = "'withProducts'")
    public List<CategoryDto> getCategoriesWithProducts() {
        logger.debug("[CATEGORY] Pobieranie kategorii z produktami");
        return categoryRepository.findCategoriesWithActiveProducts()
                .stream()
                .map(mapperService::convertToDto)
                .collect(Collectors.toList());
    }

    public List<CategoryListDto> getCategoriesForDropdown() {
        return categoryRepository.findByActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(mapperService::convertToListDto)
                .collect(Collectors.toList());
    }

    public List<CategoryDto> searchCategories(String query) {
        logger.debug("[CATEGORY] Wyszukiwanie kategorii: {}", query);
        return categoryRepository.findByNameContainingIgnoreCaseAndActiveTrue(query)
                .stream()
                .map(mapperService::convertToDto)
                .collect(Collectors.toList());
    }
}