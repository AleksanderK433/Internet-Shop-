package com.shop.internetshop.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

// Response DTO - zwracany przez API
@Getter
@Setter
public class ProductDto {
    private Long id;
    private String name;
    private String shortDescription;
    private String fullDescription;
    private String sku;
    private String slug;
    private BigDecimal price;
    private BigDecimal comparePrice;
    private Integer stockQuantity;
    private Boolean trackStock;
    private Boolean allowBackorders;
    private Boolean isInStock;
    private Boolean isLowStock;
    private Boolean isOnSale;
    private BigDecimal discountAmount;
    private BigDecimal discountPercentage;
    private BigDecimal weight;
    private String dimensions;
    private Boolean active;
    private Boolean featured;
    private List<String> imageUrls;
    private String primaryImageUrl;
    private List<String> tags;
    private Long categoryId;
    private String categoryName;
    private String categorySlug;
    private String metaTitle;
    private String metaDescription;
    private Long viewCount;
    private Long purchaseCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

