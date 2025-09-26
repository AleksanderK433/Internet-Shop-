package com.shop.internetshop.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// DTO do zwracania wyników wyszukiwania z metadanymi
@Getter
@Setter
public class ProductSearchResultDto {
    private List<ProductListDto> products;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;
    private String query;
    private ProductSearchFiltersDto appliedFilters;
}
