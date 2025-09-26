package com.shop.internetshop.product.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

// DTO do tworzenia nowego produktu
@Getter
@Setter
public class CreateProductDto {
    @NotBlank(message = "Nazwa produktu jest wymagana")
    @Size(min = 2, max = 200, message = "Nazwa produktu musi mieć od 2 do 200 znaków")
    private String name;

    @Size(max = 1000, message = "Krótki opis może mieć maksymalnie 1000 znaków")
    private String shortDescription;

    @Size(max = 5000, message = "Pełny opis może mieć maksymalnie 5000 znaków")
    private String fullDescription;

    @NotBlank(message = "SKU jest wymagane")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "SKU może zawierać tylko wielkie litery, cyfry i myślniki")
    private String sku;

    @NotBlank(message = "Slug jest wymagany")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug może zawierać tylko małe litery, cyfry i myślniki")
    private String slug;

    @NotNull(message = "Cena jest wymagana")
    @DecimalMin(value = "0.01", message = "Cena musi być większa niż 0")
    @DecimalMax(value = "999999.99", message = "Cena nie może przekraczać 999,999.99")
    private BigDecimal price;

    @DecimalMin(value = "0.00", message = "Cena porównawcza nie może być ujemna")
    private BigDecimal comparePrice;

    @NotNull(message = "Stan magazynowy jest wymagany")
    @Min(value = 0, message = "Stan magazynowy nie może być ujemny")
    private Integer stockQuantity = 0;

    @Min(value = 0, message = "Minimalny poziom zapasów nie może być ujemny")
    private Integer lowStockThreshold = 5;

    private Boolean trackStock = true;

    private Boolean allowBackorders = false;

    @DecimalMin(value = "0.00", message = "Waga nie może być ujemna")
    private BigDecimal weight;

    @Size(max = 50, message = "Wymiary nie mogą przekraczać 50 znaków")
    private String dimensions;

    private Boolean active = true;

    private Boolean featured = false;

    @Min(value = 0, message = "Kolejność sortowania nie może być ujemna")
    private Integer sortOrder = 0;

    private List<@Pattern(regexp = "^https?://.*", message = "URL obrazu musi być prawidłowy") String> imageUrls;

    private List<@Size(min = 2, max = 30, message = "Tag musi mieć od 2 do 30 znaków") String> tags;

    @NotNull(message = "Kategoria jest wymagana")
    private Long categoryId;

    @Size(max = 200, message = "Meta title może mieć maksymalnie 200 znaków")
    private String metaTitle;

    @Size(max = 500, message = "Meta description może mieć maksymalnie 500 znaków")
    private String metaDescription;
}

