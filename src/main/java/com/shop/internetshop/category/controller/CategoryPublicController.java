package com.shop.internetshop.category.controller;

import com.shop.internetshop.category.dto.CategoryDto;
import com.shop.internetshop.category.dto.CategoryListDto;
import com.shop.internetshop.category.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryPublicController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryPublicController.class);

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllActiveCategories() {
        logger.debug("[API] GET /categories - pobieranie wszystkich aktywnych kategorii");
        List<CategoryDto> categories = categoryService.getAllActiveCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/tree")
    public ResponseEntity<List<CategoryDto>> getCategoryTree() {
        logger.debug("[API] GET /categories/tree - pobieranie drzewa kategorii");
        List<CategoryDto> tree = categoryService.getRootCategories();
        return ResponseEntity.ok(tree);
    }

    @GetMapping("/with-products")
    public ResponseEntity<List<CategoryDto>> getCategoriesWithProducts() {
        logger.debug("[API] GET /categories/with-products");
        List<CategoryDto> categories = categoryService.getCategoriesWithProducts();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/dropdown")
    public ResponseEntity<List<CategoryListDto>> getCategoriesForDropdown() {
        logger.debug("[API] GET /categories/dropdown");
        List<CategoryListDto> categories = categoryService.getCategoriesForDropdown();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        logger.debug("[API] GET /categories/{}", id);
        try {
            CategoryDto category = categoryService.getCategoryById(id);
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            logger.error("[API] Kategoria nie znaleziona: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<CategoryDto> getCategoryBySlug(@PathVariable String slug) {
        logger.debug("[API] GET /categories/slug/{}", slug);
        try {
            CategoryDto category = categoryService.getCategoryBySlug(slug);
            return ResponseEntity.ok(category);
        } catch (RuntimeException e) {
            logger.error("[API] Kategoria nie znaleziona: {}", slug);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<CategoryDto>> getChildCategories(@PathVariable Long parentId) {
        logger.debug("[API] GET /categories/{}/children", parentId);
        List<CategoryDto> children = categoryService.getChildCategories(parentId);
        return ResponseEntity.ok(children);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CategoryDto>> searchCategories(@RequestParam String q) {
        logger.debug("[API] GET /categories/search?q={}", q);
        List<CategoryDto> results = categoryService.searchCategories(q);
        return ResponseEntity.ok(results);
    }
}