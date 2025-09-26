package com.shop.internetshop.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

// DTO do zarządzania stanem magazynowym
@Getter
@Setter
public class StockUpdateDto {
    @NotNull(message = "ID produktu jest wymagane")
    private Long productId;

    @NotNull(message = "Ilość jest wymagana")
    @Min(value = 0, message = "Ilość nie może być ujemna")
    private Integer quantity;

    @Size(max = 200, message = "Powód może mieć maksymalnie 200 znaków")
    private String reason;  // Powód zmiany stanu (opcjonalny)
}
