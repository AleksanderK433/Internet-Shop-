package com.shop.internetshop.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

// DTO do wyszukiwania i filtrowania
@Getter
@Setter
public class ProductSearchDto {
    private String query;  // Fraza wyszukiwania
    private Long categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Boolean inStock;
    private Boolean onSale;
    private Boolean featured;
    private List<String> tags;
    private String sortBy = "name";  // name, price, created, popularity, rating
    private String sortDirection = "asc";  // asc, desc
}
