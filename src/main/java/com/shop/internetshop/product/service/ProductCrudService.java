package com.shop.internetshop.product.service;

import com.shop.internetshop.product.dto.CreateProductDto;
import com.shop.internetshop.product.dto.ProductDto;
import com.shop.internetshop.product.dto.UpdateProductDto;
import category.model.Category;
import com.shop.internetshop.product.model.Product;
import category.repository.CategoryRepository;
import com.shop.internetshop.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class ProductCrudService {

    private static final Logger logger = LoggerFactory.getLogger(ProductCrudService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductMapperService mapperService;

    // CREATE
    public ProductDto createProduct(CreateProductDto createDto) {
        logger.info("[PRODUCT] Tworzenie nowego produktu: {}", createDto.getName());

        validateProductCreation(createDto);

        Category category = categoryRepository.findById(createDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Kategoria nie została znaleziona: " + createDto.getCategoryId()));

        Product product = new Product();
        updateProductFromDto(product, createDto);
        product.setCategory(category);

        Product saved = productRepository.save(product);
        logger.info("[PRODUCT] Produkt utworzony: {} (ID: {}, SKU: {})",
                saved.getName(), saved.getId(), saved.getSku());

        return mapperService.convertToDto(saved);
    }

    // READ
    @Cacheable(value = "products", key = "#id")
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produkt nie został znaleziony: " + id));

        product.incrementViewCount();
        productRepository.save(product);

        return mapperService.convertToDto(product);
    }

    @Cacheable(value = "products", key = "'slug-' + #slug")
    public ProductDto getProductBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Produkt nie został znaleziony: " + slug));

        product.incrementViewCount();
        productRepository.save(product);

        return mapperService.convertToDto(product);
    }

    // UPDATE
    @CacheEvict(value = {"products", "featuredProducts"}, allEntries = true)
    public ProductDto updateProduct(Long id, UpdateProductDto updateDto) {
        logger.info("[PRODUCT] Aktualizacja produktu ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produkt nie został znaleziony: " + id));

        validateProductUpdate(id, updateDto);

        if (updateDto.getCategoryId() != null &&
                !updateDto.getCategoryId().equals(product.getCategory().getId())) {
            Category category = categoryRepository.findById(updateDto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Kategoria nie została znaleziona: " + updateDto.getCategoryId()));
            product.setCategory(category);
        }

        updateProductFromUpdateDto(product, updateDto);

        Product saved = productRepository.save(product);
        logger.info("[PRODUCT] Produkt zaktualizowany: {} (ID: {})", saved.getName(), saved.getId());

        return mapperService.convertToDto(saved);
    }

    // DELETE
    @CacheEvict(value = {"products", "featuredProducts"}, allEntries = true)
    public void deleteProduct(Long id) {
        logger.info("[PRODUCT] Usuwanie produktu ID: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produkt nie został znaleziony: " + id));

        productRepository.delete(product);
        logger.info("[PRODUCT] Produkt usunięty: {} (ID: {})", product.getName(), id);
    }

    // VALIDATION
    public boolean existsBySku(String sku) {
        return productRepository.existsBySku(sku);
    }

    public boolean existsBySlug(String slug) {
        return productRepository.existsBySlug(slug);
    }

    // PRIVATE METHODS
    private void validateProductCreation(CreateProductDto dto) {
        if (productRepository.existsBySku(dto.getSku())) {
            throw new RuntimeException("Produkt z tym SKU już istnieje: " + dto.getSku());
        }

        if (productRepository.existsBySlug(dto.getSlug())) {
            throw new RuntimeException("Produkt z tym slug już istnieje: " + dto.getSlug());
        }
    }

    private void validateProductUpdate(Long id, UpdateProductDto dto) {
        if (dto.getSku() != null && productRepository.existsBySkuAndIdNot(dto.getSku(), id)) {
            throw new RuntimeException("Produkt z tym SKU już istnieje: " + dto.getSku());
        }

        if (dto.getSlug() != null && productRepository.existsBySlugAndIdNot(dto.getSlug(), id)) {
            throw new RuntimeException("Produkt z tym slug już istnieje: " + dto.getSlug());
        }
    }

    private void updateProductFromDto(Product product, CreateProductDto dto) {
        product.setName(dto.getName());
        product.setShortDescription(dto.getShortDescription());
        product.setFullDescription(dto.getFullDescription());
        product.setSku(dto.getSku());
        product.setSlug(dto.getSlug());
        product.setPrice(dto.getPrice());
        product.setComparePrice(dto.getComparePrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setLowStockThreshold(dto.getLowStockThreshold());
        product.setTrackStock(dto.getTrackStock());
        product.setAllowBackorders(dto.getAllowBackorders());
        product.setWeight(dto.getWeight());
        product.setDimensions(dto.getDimensions());
        product.setActive(dto.getActive());
        product.setFeatured(dto.getFeatured());
        product.setSortOrder(dto.getSortOrder());
        product.setMetaTitle(dto.getMetaTitle());
        product.setMetaDescription(dto.getMetaDescription());

        if (dto.getImageUrls() != null) {
            product.setImageUrls(new ArrayList<>(dto.getImageUrls()));
        }

        if (dto.getTags() != null) {
            product.setTags(new ArrayList<>(dto.getTags()));
        }
    }

    private void updateProductFromUpdateDto(Product product, UpdateProductDto dto) {
        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getShortDescription() != null) product.setShortDescription(dto.getShortDescription());
        if (dto.getFullDescription() != null) product.setFullDescription(dto.getFullDescription());
        if (dto.getSku() != null) product.setSku(dto.getSku());
        if (dto.getSlug() != null) product.setSlug(dto.getSlug());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getComparePrice() != null) product.setComparePrice(dto.getComparePrice());
        if (dto.getStockQuantity() != null) product.setStockQuantity(dto.getStockQuantity());
        if (dto.getLowStockThreshold() != null) product.setLowStockThreshold(dto.getLowStockThreshold());
        if (dto.getTrackStock() != null) product.setTrackStock(dto.getTrackStock());
        if (dto.getAllowBackorders() != null) product.setAllowBackorders(dto.getAllowBackorders());
        if (dto.getWeight() != null) product.setWeight(dto.getWeight());
        if (dto.getDimensions() != null) product.setDimensions(dto.getDimensions());
        if (dto.getActive() != null) product.setActive(dto.getActive());
        if (dto.getFeatured() != null) product.setFeatured(dto.getFeatured());
        if (dto.getSortOrder() != null) product.setSortOrder(dto.getSortOrder());
        if (dto.getMetaTitle() != null) product.setMetaTitle(dto.getMetaTitle());
        if (dto.getMetaDescription() != null) product.setMetaDescription(dto.getMetaDescription());

        if (dto.getImageUrls() != null) {
            product.setImageUrls(new ArrayList<>(dto.getImageUrls()));
        }

        if (dto.getTags() != null) {
            product.setTags(new ArrayList<>(dto.getTags()));
        }
    }
}