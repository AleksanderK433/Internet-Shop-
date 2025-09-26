package com.shop.internetshop.product.controller;

import com.shop.internetshop.product.dto.*;
import com.shop.internetshop.product.service.ProductCrudService;
import com.shop.internetshop.product.service.ProductStockService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/products")
@PreAuthorize("hasRole('ADMIN')")
public class ProductAdminController {

    private static final Logger logger = LoggerFactory.getLogger(ProductAdminController.class);

    @Autowired
    private ProductCrudService productCrudService;

    @Autowired
    private ProductStockService productStockService;

    // CRUD OPERATIONS

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody CreateProductDto createDto) {
        logger.info("[ADMIN] POST /admin/products - tworzenie produktu: {}", createDto.getName());
        try {
            ProductDto created = productCrudService.createProduct(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            logger.error("[ADMIN] Błąd tworzenia produktu: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductDto updateDto) {
        logger.info("[ADMIN] PUT /admin/products/{} - aktualizacja produktu", id);
        try {
            ProductDto updated = productCrudService.updateProduct(id, updateDto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            logger.error("[ADMIN] Błąd aktualizacji produktu {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteProduct(@PathVariable Long id) {
        logger.info("[ADMIN] DELETE /admin/products/{} - usuwanie produktu", id);
        try {
            productCrudService.deleteProduct(id);
            return ResponseEntity.ok(Map.of("message", "Produkt został usunięty"));
        } catch (RuntimeException e) {
            logger.error("[ADMIN] Błąd usuwania produktu {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // STOCK MANAGEMENT

    @PostMapping("/stock/update")
    public ResponseEntity<Map<String, String>> updateStock(@Valid @RequestBody StockUpdateDto stockUpdate) {
        logger.info("[ADMIN] POST /admin/products/stock/update - aktualizacja stanu dla produktu ID: {}",
                stockUpdate.getProductId());
        try {
            productStockService.updateStock(stockUpdate);
            return ResponseEntity.ok(Map.of("message", "Stan magazynowy został zaktualizowany"));
        } catch (RuntimeException e) {
            logger.error("[ADMIN] Błąd aktualizacji stanu: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/stock/decrement")
    public ResponseEntity<Map<String, Object>> decrementStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        logger.info("[ADMIN] POST /admin/products/{}/stock/decrement - zmniejszenie o: {}", id, quantity);
        try {
            boolean success = productStockService.decrementStock(id, quantity);
            return ResponseEntity.ok(Map.of(
                    "success", success,
                    "message", success ? "Stan magazynowy zmniejszony" : "Niewystarczająca ilość na stanie"
            ));
        } catch (RuntimeException e) {
            logger.error("[ADMIN] Błąd zmniejszania stanu: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{id}/stock/increment")
    public ResponseEntity<Map<String, String>> incrementStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        logger.info("[ADMIN] POST /admin/products/{}/stock/increment - zwiększenie o: {}", id, quantity);
        try {
            productStockService.incrementStock(id, quantity);
            return ResponseEntity.ok(Map.of("message", "Stan magazynowy zwiększony"));
        } catch (RuntimeException e) {
            logger.error("[ADMIN] Błąd zwiększania stanu: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stock/low")
    public ResponseEntity<List<ProductListDto>> getLowStockProducts() {
        logger.debug("[ADMIN] GET /admin/products/stock/low");
        List<ProductListDto> products = productStockService.getLowStockProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/stock/out")
    public ResponseEntity<List<ProductListDto>> getOutOfStockProducts() {
        logger.debug("[ADMIN] GET /admin/products/stock/out");
        List<ProductListDto> products = productStockService.getOutOfStockProducts();
        return ResponseEntity.ok(products);
    }

    // EXCEPTION HANDLING

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        logger.error("[ADMIN] Runtime exception: {}", e.getMessage());
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("[ADMIN] Illegal argument: {}", e.getMessage());
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }
}