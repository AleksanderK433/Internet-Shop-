package com.shop.internetshop.category.repository;

import com.shop.internetshop.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    // Podstawowe queries
    Optional<Category> findBySlug(String slug);

    List<Category> findByActiveTrue();

    List<Category> findByActiveTrueOrderBySortOrderAsc();

    // Hierarchia kategorii
    List<Category> findByParentIsNullAndActiveTrue();  // Root categories

    List<Category> findByParentAndActiveTrue(Category parent);  // Child categories

    @Query("SELECT c FROM Category c WHERE c.parent.id = :parentId AND c.active = true ORDER BY c.sortOrder ASC")
    List<Category> findActiveChildrenByParentId(@Param("parentId") Long parentId);

    // Sprawdź czy kategoria ma produkty
    @Query("SELECT COUNT(p) > 0 FROM Product p WHERE p.category = :category AND p.active = true")
    boolean hasActiveProducts(@Param("com/shop/internetshop/category") Category category);

    // Znajdź kategorie z produktami
    @Query("SELECT DISTINCT c FROM Category c JOIN c.products p WHERE c.active = true AND p.active = true ORDER BY c.sortOrder ASC")
    List<Category> findCategoriesWithActiveProducts();

    // Wyszukiwanie
    List<Category> findByNameContainingIgnoreCaseAndActiveTrue(String name);

    // Statystyki
    @Query("SELECT COUNT(c) FROM Category c WHERE c.parent IS NULL")
    long countRootCategories();

    @Query("SELECT COUNT(c) FROM Category c WHERE c.parent IS NOT NULL")
    long countChildCategories();

    // Sprawdź czy slug jest unikalny (dla walidacji)
    boolean existsBySlugAndIdNot(String slug, Long id);

    boolean existsBySlug(String slug);

    // Znajdź wszystkie kategorie potomne (rekursywnie)
    @Query(value = "WITH RECURSIVE category_tree AS (" +
            "  SELECT id, name, slug, parent_id, 0 as level" +
            "  FROM categories WHERE id = :categoryId" +
            "  UNION ALL" +
            "  SELECT c.id, c.name, c.slug, c.parent_id, ct.level + 1" +
            "  FROM categories c" +
            "  JOIN category_tree ct ON c.parent_id = ct.id" +
            ") SELECT * FROM category_tree WHERE level > 0",
            nativeQuery = true)
    List<Category> findAllDescendants(@Param("categoryId") Long categoryId);

    // Sprawdź czy można usunąć kategorię (nie ma dzieci ani produktów)
    @Query("SELECT COUNT(c) = 0 AND COUNT(p) = 0 FROM Category c LEFT JOIN c.products p WHERE c.parent.id = :categoryId OR p.category.id = :categoryId")
    boolean canBeDeleted(@Param("categoryId") Long categoryId);
}