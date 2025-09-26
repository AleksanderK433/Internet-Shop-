package com.shop.internetshop.product.service;

import com.shop.internetshop.product.dto.ProductListDto;
import com.shop.internetshop.product.dto.ProductSearchDto;
import com.shop.internetshop.product.dto.ProductSearchResultDto;
import category.model.Category;
import com.shop.internetshop.product.model.Product;
import category.repository.CategoryRepository;
import com.shop.internetshop.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductSearchService {

    private static final Logger logger = LoggerFactory.getLogger(ProductSearchService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapperService mapperService;

    public Page<ProductListDto> getAllProducts(int page, int size, String sortBy, String sortDir) {
        logger.debug("[PRODUCT] Pobieranie wszystkich produktów - strona: {}, rozmiar: {}", page, size);

        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findByActiveTrue(pageable)
                .map(mapperService::convertToListDto);
    }

    public Page<ProductListDto> getProductsByCategory(Long categoryId, int page, int size, String sortBy, String sortDir) {
        logger.debug("[PRODUCT] Pobieranie produktów dla kategorii: {}", categoryId);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Kategoria nie została znaleziona: " + categoryId));

        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        return productRepository.findByCategoryAndActiveTrue(category, pageable)
                .map(mapperService::convertToListDto);
    }

    public List<ProductListDto> getFeaturedProducts() {
        logger.debug("[PRODUCT] Pobieranie produktów wyróżnionych");
        return productRepository.findByFeaturedTrueAndActiveTrueOrderBySortOrderAsc()
                .stream()
                .map(mapperService::convertToListDto)
                .collect(Collectors.toList());
    }

    public ProductSearchResultDto searchProducts(ProductSearchDto searchDto, int page, int size) {
        logger.debug("[PRODUCT] Wyszukiwanie produktów: {}", searchDto.getQuery());

        Sort sort = createSort(searchDto.getSortBy(), searchDto.getSortDirection());
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage;

        if (searchDto.getQuery() != null && !searchDto.getQuery().trim().isEmpty()) {
            productPage = productRepository.searchProducts(searchDto.getQuery().trim(), pageable);
        } else {
            productPage = productRepository.findWithFilters(
                    searchDto.getMinPrice(),
                    searchDto.getMaxPrice(),
                    searchDto.getCategoryId(),
                    pageable
            );
        }

        // Aplikuj dodatkowe filtry w pamięci
        List<ProductListDto> filteredProducts = productPage.getContent().stream()
                .filter(product -> applyAdditionalFilters(product, searchDto))
                .map(mapperService::convertToListDto)
                .collect(Collectors.toList());

        ProductSearchResultDto result = new ProductSearchResultDto();
        result.setProducts(filteredProducts);
        result.setTotalElements(productPage.getTotalElements());
        result.setTotalPages(productPage.getTotalPages());
        result.setCurrentPage(page);
        result.setPageSize(size);
        result.setQuery(searchDto.getQuery());

        return result;
    }

    public Page<ProductListDto> getBestsellers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findBestsellers(pageable)
                .map(mapperService::convertToListDto);
    }

    public Page<ProductListDto> getNewestProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findNewestProducts(pageable)
                .map(mapperService::convertToListDto);
    }

    public Page<ProductListDto> getOnSaleProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findOnSaleProducts(pageable)
                .map(mapperService::convertToListDto);
    }

    // HELPER METHODS
    private boolean applyAdditionalFilters(Product product, ProductSearchDto searchDto) {
        if (searchDto.getInStock() != null && searchDto.getInStock() && !product.isInStock()) {
            return false;
        }

        if (searchDto.getOnSale() != null && searchDto.getOnSale() && !product.isOnSale()) {
            return false;
        }

        if (searchDto.getFeatured() != null && searchDto.getFeatured() != product.getFeatured()) {
            return false;
        }

        return true;
    }

    private Sort createSort(String sortBy, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;

        return switch (sortBy) {
            case "price" -> Sort.by(direction, "price");
            case "created" -> Sort.by(direction, "createdAt");
            case "popularity" -> Sort.by(direction, "viewCount");
            case "purchases" -> Sort.by(direction, "purchaseCount");
            default -> Sort.by(direction, "name");
        };
    }
}