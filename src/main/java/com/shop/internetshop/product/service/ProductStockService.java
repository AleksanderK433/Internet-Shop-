package com.shop.internetshop.product.service;


import com.shop.internetshop.product.dto.ProductListDto;
import com.shop.internetshop.product.dto.StockUpdateDto;
import com.shop.internetshop.product.model.Product;
import com.shop.internetshop.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductStockService {

    private static final Logger logger = LoggerFactory.getLogger(ProductStockService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapperService mapperService;

    /**
     * Aktualizuje stan magazynowy produktu do określonej wartości
     */
    public void updateStock(StockUpdateDto stockUpdate) {
        logger.info("[STOCK] Aktualizacja stanu magazynowego produktu ID: {} na {}",
                stockUpdate.getProductId(), stockUpdate.getQuantity());

        Product product = productRepository.findById(stockUpdate.getProductId())
                .orElseThrow(() -> new RuntimeException("Produkt nie został znaleziony: " + stockUpdate.getProductId()));

        Integer oldQuantity = product.getStockQuantity();
        product.setStockQuantity(stockUpdate.getQuantity());
        productRepository.save(product);

        logger.info("[STOCK] Stan magazynowy zaktualizowany dla {} z {} na {} (powód: {})",
                product.getName(), oldQuantity, stockUpdate.getQuantity(),
                stockUpdate.getReason() != null ? stockUpdate.getReason() : "Nie podano");
    }

    /**
     * Zmniejsza stan magazynowy o określoną ilość (bezpieczne - sprawdza dostępność)
     */
    public boolean decrementStock(Long productId, Integer quantity) {
        logger.debug("[STOCK] Zmniejszanie stanu magazynowego produktu ID: {} o {}", productId, quantity);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Ilość musi być większa niż 0");
        }

        int updated = productRepository.decrementStock(productId, quantity);
        boolean success = updated > 0;

        if (success) {
            logger.info("[STOCK] Stan magazynowy zmniejszony dla produktu ID: {} o {}", productId, quantity);
        } else {
            logger.warn("[STOCK] Nie udało się zmniejszyć stanu magazynowego - brak wystarczającej ilości (produktID: {}, żądana ilość: {})",
                    productId, quantity);
        }

        return success;
    }

    /**
     * Zwiększa stan magazynowy o określoną ilość
     */
    public void incrementStock(Long productId, Integer quantity) {
        logger.debug("[STOCK] Zwiększanie stanu magazynowego produktu ID: {} o {}", productId, quantity);

        if (quantity <= 0) {
            throw new IllegalArgumentException("Ilość musi być większa niż 0");
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produkt nie został znaleziony: " + productId));

        Integer oldQuantity = product.getStockQuantity();
        productRepository.incrementStock(productId, quantity);

        logger.info("[STOCK] Stan magazynowy zwiększony dla {} z {} o {} (nowy stan: {})",
                product.getName(), oldQuantity, quantity, oldQuantity + quantity);
    }

    /**
     * Zwraca produkty z niskim stanem magazynowym
     */
    public List<ProductListDto> getLowStockProducts() {
        logger.debug("[STOCK] Pobieranie produktów z niskim stanem magazynowym");
        List<Product> products = productRepository.findLowStockProducts();

        logger.info("[STOCK] Znaleziono {} produktów z niskim stanem magazynowym", products.size());

        return products.stream()
                .map(mapperService::convertToListDto)
                .collect(Collectors.toList());
    }

    /**
     * Zwraca produkty bez stanu magazynowego (stockQuantity = 0)
     */
    public List<ProductListDto> getOutOfStockProducts() {
        logger.debug("[STOCK] Pobieranie produktów bez stanu magazynowego");
        List<Product> products = productRepository.findOutOfStockProducts();

        logger.info("[STOCK] Znaleziono {} produktów bez stanu magazynowego", products.size());

        return products.stream()
                .map(mapperService::convertToListDto)
                .collect(Collectors.toList());
    }

    /**
     * Zwraca produkty z określonym progiem stanu magazynowego
     */
    public List<ProductListDto> getProductsWithStockBelow(Integer threshold) {
        logger.debug("[STOCK] Pobieranie produktów z stanem magazynowym poniżej: {}", threshold);
        List<Product> products = productRepository.findByTrackStockTrueAndStockQuantityLessThanEqualAndActiveTrue(threshold);

        return products.stream()
                .map(mapperService::convertToListDto)
                .collect(Collectors.toList());
    }

    /**
     * Zwraca produkty z dokładnie określonym stanem magazynowym
     */
    public List<ProductListDto> getProductsWithExactStock(Integer quantity) {
        logger.debug("[STOCK] Pobieranie produktów z dokładnym stanem magazynowym: {}", quantity);
        List<Product> products = productRepository.findByTrackStockTrueAndStockQuantityEquals(quantity);

        return products.stream()
                .map(mapperService::convertToListDto)
                .collect(Collectors.toList());
    }

    /**
     * Sprawdza czy produkt ma wystarczający stan magazynowy
     */
    public boolean hasEnoughStock(Long productId, Integer requiredQuantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produkt nie został znaleziony: " + productId));

        if (!product.getTrackStock()) {
            return true; // Jeśli nie śledzimy stanu, zawsze dostępny
        }

        boolean hasEnough = product.getStockQuantity() >= requiredQuantity;

        logger.debug("[STOCK] Produkt {} - wymagane: {}, dostępne: {}, wystarczy: {}",
                product.getName(), requiredQuantity, product.getStockQuantity(), hasEnough);

        return hasEnough;
    }

    /**
     * Zwraca aktualny stan magazynowy produktu
     */
    public Integer getCurrentStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produkt nie został znaleziony: " + productId));

        return product.getStockQuantity();
    }

    /**
     * Sprawdza czy produkt można kupić (uwzględnia backorders)
     */
    public boolean canPurchase(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Produkt nie został znaleziony: " + productId));

        if (!product.getActive()) {
            return false;
        }

        if (!product.getTrackStock()) {
            return true;
        }

        boolean hasStock = product.getStockQuantity() >= quantity;
        boolean allowBackorder = product.getAllowBackorders();

        boolean canPurchase = hasStock || allowBackorder;

        logger.debug("[STOCK] Produkt {} - można kupić {}: {} (stan: {}, backorder: {})",
                product.getName(), quantity, canPurchase, product.getStockQuantity(), allowBackorder);

        return canPurchase;
    }
}