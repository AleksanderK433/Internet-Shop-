package com.shop.internetshop.product.model.enums;

public enum StockStatus {
    /**
     * Dostępny na stanie
     */
    IN_STOCK,

    /**
     * Niski stan magazynowy
     */
    LOW_STOCK,

    /**
     * Brak na stanie
     */
    OUT_OF_STOCK,

    /**
     * Dostępny na zamówienie
     */
    BACKORDER
}
