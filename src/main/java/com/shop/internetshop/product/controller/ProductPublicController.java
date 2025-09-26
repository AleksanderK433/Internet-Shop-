package com.shop.internetshop.product.controller;

import com.shop.internetshop.product.dto.ProductDto;
import com.shop.internetshop.product.dto.ProductListDto;
import com.shop.internetshop.product.dto.ProductSearchDto;
import com.shop.internetshop.product.dto.ProductSearchResultDto;
import com.shop.internetshop.product.service.ProductCrudService;
import com.shop.internetshop.product.service.ProductSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductPublicController {

    private static final Logger logger = LoggerFactory.getLogger(ProductAdminController.class);

    @Autowired
    private ProductCrudService productCrudService;

    @Autowired
    private ProductSearchService productSearchService;

    @GetMapping
    public ResponseEntity<Page<ProductListDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        logger.debug("[API] GET /products - strona: {}, rozmiar: {}", page, size);
        Page<ProductListDto> products = productSearchService.getAllProducts(page, size, sortBy, sortDir);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        logger.debug("[API] GET /products/{}", id);
        ProductDto product = productCrudService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ProductDto> getProductBySlug(@PathVariable String slug) {
        logger.debug("[API] GET /products/slug/{}", slug);
        ProductDto product = productCrudService.getProductBySlug(slug);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductListDto>> getProductsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        logger.debug("[API] GET /products/category/{}", categoryId);
        Page<ProductListDto> products = productSearchService.getProductsByCategory(categoryId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/featured")
    public ResponseEntity<List<ProductListDto>> getFeaturedProducts() {
        logger.debug("[API] GET /products/featured");
        List<ProductListDto> products = productSearchService.getFeaturedProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/bestsellers")
    public ResponseEntity<Page<ProductListDto>> getBestsellers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        logger.debug("[API] GET /products/bestsellers");
        Page<ProductListDto> products = productSearchService.getBestsellers(page, size);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/newest")
    public ResponseEntity<Page<ProductListDto>> getNewestProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        logger.debug("[API] GET /products/newest");
        Page<ProductListDto> products = productSearchService.getNewestProducts(page, size);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/on-sale")
    public ResponseEntity<Page<ProductListDto>> getOnSaleProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        logger.debug("[API] GET /products/on-sale");
        Page<ProductListDto> products = productSearchService.getOnSaleProducts(page, size);
        return ResponseEntity.ok(products);
    }

    @PostMapping("/search")
    public ResponseEntity<ProductSearchResultDto> searchProducts(
            @RequestBody ProductSearchDto searchDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        logger.debug("[API] POST /products/search - zapytanie: {}", searchDto.getQuery());
        ProductSearchResultDto results = productSearchService.searchProducts(searchDto, page, size);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/search")
    public ResponseEntity<ProductSearchResultDto> searchProductsGet(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String minPrice,
            @RequestParam(required = false) String maxPrice,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) Boolean onSale,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        logger.debug("[API] GET /products/search?q={}", q);

        ProductSearchDto searchDto = new ProductSearchDto();
        searchDto.setQuery(q);
        searchDto.setCategoryId(categoryId);
        searchDto.setInStock(inStock);
        searchDto.setOnSale(onSale);
        searchDto.setFeatured(featured);
        searchDto.setSortBy(sortBy);
        searchDto.setSortDirection(sortDir);

        // Parse price parameters
        try {
            if (minPrice != null && !minPrice.isEmpty()) {
                searchDto.setMinPrice(java.math.BigDecimal.valueOf(Double.parseDouble(minPrice)));
            }
            if (maxPrice != null && !maxPrice.isEmpty()) {
                searchDto.setMaxPrice(java.math.BigDecimal.valueOf(Double.parseDouble(maxPrice)));
            }
        } catch (NumberFormatException e) {
            logger.warn("[API] Invalid price format: minPrice={}, maxPrice={}", minPrice, maxPrice);
        }

        ProductSearchResultDto results = productSearchService.searchProducts(searchDto, page, size);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/check-sku/{sku}")
    public ResponseEntity<Map<String, Boolean>> checkSkuAvailability(@PathVariable String sku) {
        logger.debug("[API] GET /products/check-sku/{}", sku);
        boolean exists = productCrudService.existsBySku(sku);
        return ResponseEntity.ok(Map.of("available", !exists));
    }

    @GetMapping("/check-slug/{slug}")
    public ResponseEntity<Map<String, Boolean>> checkSlugAvailability(@PathVariable String slug) {
        logger.debug("[API] GET /products/check-slug/{}", slug);
        boolean exists = productCrudService.existsBySlug(slug);
        return ResponseEntity.ok(Map.of("available", !exists));
    }
}
