package com.shop.internetshop.product.model;

import category.model.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa produktu jest wymagana")
    @Size(min = 2, max = 200, message = "Nazwa produktu musi mieć od 2 do 200 znaków")
    @Column(nullable = false)
    private String name;

    @Size(max = 1000, message = "Krótki opis może mieć maksymalnie 1000 znaków")
    @Column(name = "short_description", columnDefinition = "TEXT")
    private String shortDescription;

    @Size(max = 5000, message = "Pełny opis może mieć maksymalnie 5000 znaków")
    @Column(name = "full_description", columnDefinition = "TEXT")
    private String fullDescription;

    @NotBlank(message = "SKU jest wymagane")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "SKU może zawierać tylko wielkie litery, cyfry i myślniki")
    @Column(unique = true, nullable = false)
    private String sku;  // Stock Keeping Unit

    @NotBlank(message = "Slug jest wymagany")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug może zawierać tylko małe litery, cyfry i myślniki")
    @Column(unique = true, nullable = false)
    private String slug;  // SEO-friendly URL

    @NotNull(message = "Cena jest wymagana")
    @DecimalMin(value = "0.01", message = "Cena musi być większa niż 0")
    @DecimalMax(value = "999999.99", message = "Cena nie może przekraczać 999,999.99")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @DecimalMin(value = "0.00", message = "Cena porównawcza nie może być ujemna")
    @Column(name = "compare_price", precision = 10, scale = 2)
    private BigDecimal comparePrice;  // Cena przed promocją

    @NotNull(message = "Stan magazynowy jest wymagany")
    @Min(value = 0, message = "Stan magazynowy nie może być ujemny")
    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity = 0;

    @Min(value = 0, message = "Minimalny poziom zapasów nie może być ujemny")
    @Column(name = "low_stock_threshold")
    private Integer lowStockThreshold = 5;

    @Column(name = "track_stock")
    private Boolean trackStock = true;

    @Column(name = "allow_backorders")
    private Boolean allowBackorders = false;

    @DecimalMin(value = "0.00", message = "Waga nie może być ujemna")
    @Column(precision = 8, scale = 3)
    private BigDecimal weight;  // w kilogramach

    @Size(max = 50)
    private String dimensions;  // np. "30x20x10 cm"

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private Boolean featured = false;  // Produkt wyróżniony

    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    // Zdjęcia produktu
    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> imageUrls = new ArrayList<>();

    // Tagi/słowa kluczowe
    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Metadane SEO
    @Size(max = 200)
    @Column(name = "meta_title")
    private String metaTitle;

    @Size(max = 500)
    @Column(name = "meta_description")
    private String metaDescription;

    // Statystyki
    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column(name = "purchase_count")
    private Long purchaseCount = 0L;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Konstruktory
    public Product() {}

    public Product(String name, String sku, String slug, BigDecimal price, Category category) {
        this.name = name;
        this.sku = sku;
        this.slug = slug;
        this.price = price;
        this.category = category;
    }

    // Helper methods
    public boolean isInStock() {
        if (!trackStock) return true;
        return stockQuantity > 0;
    }

    public boolean isLowStock() {
        if (!trackStock) return false;
        return stockQuantity <= lowStockThreshold;
    }

    public boolean isOnSale() {
        return comparePrice != null && comparePrice.compareTo(price) > 0;
    }

    public BigDecimal getDiscountAmount() {
        if (!isOnSale()) return BigDecimal.ZERO;
        return comparePrice.subtract(price);
    }

    public BigDecimal getDiscountPercentage() {
        if (!isOnSale()) return BigDecimal.ZERO;
        return getDiscountAmount()
                .divide(comparePrice, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public String getPrimaryImageUrl() {
        return imageUrls != null && !imageUrls.isEmpty() ? imageUrls.get(0) : null;
    }

    public boolean canPurchase() {
        if (!active) return false;
        if (!trackStock) return true;
        return stockQuantity > 0 || allowBackorders;
    }

    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null ? 0 : this.viewCount) + 1;
    }

    public void incrementPurchaseCount() {
        this.purchaseCount = (this.purchaseCount == null ? 0 : this.purchaseCount) + 1;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", active=" + active +
                '}';
    }
}