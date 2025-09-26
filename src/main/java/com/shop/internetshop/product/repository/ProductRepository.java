package com.shop.internetshop.product.repository;

import com.shop.internetshop.category.model.Category;
import com.shop.internetshop.product.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Podstawowe queries
    Optional<Product> findBySlug(String slug);

    Optional<Product> findBySku(String sku);

    List<Product> findByActiveTrue();

    Page<Product> findByActiveTrue(Pageable pageable);

    // Kategorie
    List<Product> findByCategoryAndActiveTrue(Category category);

    Page<Product> findByCategoryAndActiveTrue(Category category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.category.id IN :categoryIds AND p.active = true")
    Page<Product> findByCategoryIdInAndActiveTrue(@Param("categoryIds") List<Long> categoryIds, Pageable pageable);

    // Wyszukiwanie
    @Query("SELECT p FROM Product p WHERE " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.shortDescription) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.sku) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
            "p.active = true")
    Page<Product> searchProducts(@Param("query") String query, Pageable pageable);

    // Filtry cenowe
    Page<Product> findByActiveTrueAndPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.active = true AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Product> findWithFilters(@Param("minPrice") BigDecimal minPrice,
                                  @Param("maxPrice") BigDecimal maxPrice,
                                  @Param("categoryId") Long categoryId,
                                  Pageable pageable);

    // Stan magazynowy
    List<Product> findByTrackStockTrueAndStockQuantityLessThanEqualAndActiveTrue(Integer threshold);

    List<Product> findByTrackStockTrueAndStockQuantityEquals(Integer quantity);

    @Query("SELECT p FROM Product p WHERE p.trackStock = true AND p.stockQuantity <= p.lowStockThreshold AND p.active = true")
    List<Product> findLowStockProducts();

    @Query("SELECT p FROM Product p WHERE p.trackStock = true AND p.stockQuantity = 0 AND p.active = true")
    List<Product> findOutOfStockProducts();

    // Produkty wyróżnione
    List<Product> findByFeaturedTrueAndActiveTrueOrderBySortOrderAsc();

    Page<Product> findByFeaturedTrueAndActiveTrue(Pageable pageable);

    // Bestsellery
    @Query("SELECT p FROM Product p WHERE p.active = true ORDER BY p.purchaseCount DESC")
    Page<Product> findBestsellers(Pageable pageable);

    // Najczęściej oglądane
    @Query("SELECT p FROM Product p WHERE p.active = true ORDER BY p.viewCount DESC")
    Page<Product> findMostViewed(Pageable pageable);

    // Produkty w promocji
    @Query("SELECT p FROM Product p WHERE p.active = true AND p.comparePrice IS NOT NULL AND p.comparePrice > p.price")
    Page<Product> findOnSaleProducts(Pageable pageable);

    // Najnowsze produkty
    @Query("SELECT p FROM Product p WHERE p.active = true ORDER BY p.createdAt DESC")
    Page<Product> findNewestProducts(Pageable pageable);

    // Statystyki
    long countByActiveTrue();

    long countByCategory(Category category);

    long countByActiveTrueAndFeaturedTrue();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.active = true AND p.trackStock = true AND p.stockQuantity = 0")
    long countOutOfStockProducts();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.active = true AND p.trackStock = true AND p.stockQuantity <= p.lowStockThreshold")
    long countLowStockProducts();

    // Walidacje
    boolean existsBySkuAndIdNot(String sku, Long id);

    boolean existsBySlugAndIdNot(String slug, Long id);

    boolean existsBySku(String sku);

    boolean existsBySlug(String slug);

    // Aktualizacje
    @Modifying
    @Query("UPDATE Product p SET p.viewCount = p.viewCount + 1 WHERE p.id = :productId")
    void incrementViewCount(@Param("productId") Long productId);

    @Modifying
    @Query("UPDATE Product p SET p.purchaseCount = p.purchaseCount + 1 WHERE p.id = :productId")
    void incrementPurchaseCount(@Param("productId") Long productId);

    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity - :quantity WHERE p.id = :productId AND p.stockQuantity >= :quantity")
    int decrementStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity + :quantity WHERE p.id = :productId")
    void incrementStock(@Param("productId") Long productId, @Param("quantity") Integer quantity);

    // Zaawansowane wyszukiwanie z tagami
    @Query("SELECT DISTINCT p FROM Product p JOIN p.tags t WHERE " +
            "p.active = true AND LOWER(t) IN :tags")
    Page<Product> findByTagsInIgnoreCase(@Param("tags") List<String> tags, Pageable pageable);

    // Podobne produkty (ta sama kategoria, wyklucz obecny)
    @Query("SELECT p FROM Product p WHERE p.category = :category AND p.active = true AND p.id != :excludeId ORDER BY FUNCTION('RAND')")
    List<Product> findSimilarProducts(@Param("com/shop/internetshop/category") Category category, @Param("excludeId") Long excludeId, Pageable pageable);
}