package com.shop.internetshop.product.dto;


import category.dto.CategoryListDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

// DTO z dostępnymi filtrami
@Getter
@Setter
public class ProductSearchFiltersDto {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private List<CategoryListDto> categories;
    private List<String> availableTags;
    private Long totalProducts;
}
