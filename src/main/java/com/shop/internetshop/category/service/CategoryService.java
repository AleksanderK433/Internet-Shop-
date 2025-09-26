package com.shop.internetshop.category.service;

import com.shop.internetshop.category.dto.CategoryListDto;
import com.shop.internetshop.category.dto.CategoryDto;
import com.shop.internetshop.category.dto.CreateCategoryDto;
import com.shop.internetshop.category.dto.UpdateCategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Główny serwis kategorii - orchestrator dla wszystkich operacji
 * Deleguje wywołania do specjalistycznych serwisów
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryCrudService crudService;

    @Autowired
    private CategorySearchService searchService;

    // CRUD operations - deleguj do CategoryCrudService
    public CategoryDto createCategory(CreateCategoryDto createDto) {
        return crudService.createCategory(createDto);
    }

    public CategoryDto getCategoryById(Long id) {
        return crudService.getCategoryById(id);
    }

    public CategoryDto getCategoryBySlug(String slug) {
        return crudService.getCategoryBySlug(slug);
    }

    public CategoryDto updateCategory(Long id, UpdateCategoryDto updateDto) {
        return crudService.updateCategory(id, updateDto);
    }

    public void deleteCategory(Long id) {
        crudService.deleteCategory(id);
    }

    // Search operations - deleguj do CategorySearchService
    public List<CategoryDto> getAllActiveCategories() {
        return searchService.getAllActiveCategories();
    }

    public List<CategoryDto> getRootCategories() {
        return searchService.getRootCategories();
    }

    public List<CategoryDto> getChildCategories(Long parentId) {
        return searchService.getChildCategories(parentId);
    }

    public List<CategoryDto> getCategoriesWithProducts() {
        return searchService.getCategoriesWithProducts();
    }

    public List<CategoryListDto> getCategoriesForDropdown() {
        return searchService.getCategoriesForDropdown();
    }

    public List<CategoryDto> searchCategories(String query) {
        return searchService.searchCategories(query);
    }
}
