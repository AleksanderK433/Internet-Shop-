package com.shop.internetshop.category.service;

import com.shop.internetshop.category.dto.CategoryDto;
import com.shop.internetshop.category.dto.CategoryListDto;
import com.shop.internetshop.category.model.Category;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CategoryMapperService {

    public CategoryDto convertToDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());
        dto.setSlug(category.getSlug());
        dto.setImageUrl(category.getImageUrl());
        dto.setActive(category.getActive());
        dto.setSortOrder(category.getSortOrder());
        dto.setLevel(category.getLevel());
        dto.setFullPath(category.getFullPath());
        dto.setHasChildren(category.hasChildren());
        dto.setHasProducts(category.hasProducts());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());

        if (category.getParent() != null) {
            dto.setParentId(category.getParent().getId());
            dto.setParentName(category.getParent().getName());
        }

        if (category.getProducts() != null) {
            dto.setProductCount(category.getProducts().size());
        }

        return dto;
    }

    public CategoryDto convertToDtoWithChildren(Category category) {
        CategoryDto dto = convertToDto(category);

        if (category.hasChildren()) {
            dto.setChildren(
                    category.getChildren().stream()
                            .filter(Category::getActive)
                            .map(this::convertToDtoWithChildren)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public CategoryListDto convertToListDto(Category category) {
        CategoryListDto dto = new CategoryListDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setSlug(category.getSlug());
        dto.setLevel(category.getLevel());
        dto.setFullPath(category.getFullPath());
        dto.setHasProducts(category.hasProducts());

        if (category.getProducts() != null) {
            dto.setProductCount(category.getProducts().size());
        }

        return dto;
    }
}