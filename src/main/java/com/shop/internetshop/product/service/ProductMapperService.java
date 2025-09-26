package com.shop.internetshop.product.service;

import com.shop.internetshop.product.dto.ProductDto;
import com.shop.internetshop.product.dto.ProductListDto;
import com.shop.internetshop.product.model.Product;
import org.springframework.stereotype.Service;

@Service
public class ProductMapperService {

    public ProductDto convertToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setShortDescription(product.getShortDescription());
        dto.setFullDescription(product.getFullDescription());
        dto.setSku(product.getSku());
        dto.setSlug(product.getSlug());
        dto.setPrice(product.getPrice());
        dto.setComparePrice(product.getComparePrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setTrackStock(product.getTrackStock());
        dto.setAllowBackorders(product.getAllowBackorders());
        dto.setIsInStock(product.isInStock());
        dto.setIsLowStock(product.isLowStock());
        dto.setIsOnSale(product.isOnSale());
        dto.setDiscountAmount(product.getDiscountAmount());
        dto.setDiscountPercentage(product.getDiscountPercentage());
        dto.setWeight(product.getWeight());
        dto.setDimensions(product.getDimensions());
        dto.setActive(product.getActive());
        dto.setFeatured(product.getFeatured());
        dto.setImageUrls(product.getImageUrls());
        dto.setPrimaryImageUrl(product.getPrimaryImageUrl());
        dto.setTags(product.getTags());
        dto.setMetaTitle(product.getMetaTitle());
        dto.setMetaDescription(product.getMetaDescription());
        dto.setViewCount(product.getViewCount());
        dto.setPurchaseCount(product.getPurchaseCount());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getId());
            dto.setCategoryName(product.getCategory().getName());
            dto.setCategorySlug(product.getCategory().getSlug());
        }

        return dto;
    }

    public ProductListDto convertToListDto(Product product) {
        ProductListDto dto = new ProductListDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setSku(product.getSku());
        dto.setSlug(product.getSlug());
        dto.setPrice(product.getPrice());
        dto.setComparePrice(product.getComparePrice());
        dto.setIsOnSale(product.isOnSale());
        dto.setDiscountPercentage(product.getDiscountPercentage());
        dto.setIsInStock(product.isInStock());
        dto.setPrimaryImageUrl(product.getPrimaryImageUrl());
        dto.setFeatured(product.getFeatured());
        dto.setViewCount(product.getViewCount());
        dto.setCreatedAt(product.getCreatedAt());

        if (product.getCategory() != null) {
            dto.setCategoryName(product.getCategory().getName());
        }

        return dto;
    }
}
