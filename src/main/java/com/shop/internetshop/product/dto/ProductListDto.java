package com.shop.internetshop.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class ProductListDto {
    private Long id;
    private String name;
    private String sku;
    private String slug;
    private BigDecimal price;
    private BigDecimal comparePrice;
    private Boolean isOnSale;
    private BigDecimal discountPercentage;
    private Boolean isInStock;
    private String primaryImageUrl;
    private String categoryName;
    private Boolean featured;
    private Long viewCount;
    private LocalDateTime createdAt;
}
