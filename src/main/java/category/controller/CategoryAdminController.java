package category.controller;

import category.dto.CategoryDto;
import category.dto.CreateCategoryDto;
import category.dto.UpdateCategoryDto;
import category.service.CategoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin/categories")
@PreAuthorize("hasRole('ADMIN')")
public class CategoryAdminController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryAdminController.class);

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryDto createDto) {
        logger.info("[ADMIN] POST /admin/categories - tworzenie kategorii: {}", createDto.getName());
        try {
            CategoryDto created = categoryService.createCategory(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            logger.error("[ADMIN] Błąd tworzenia kategorii: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryDto updateDto) {
        logger.info("[ADMIN] PUT /admin/categories/{} - aktualizacja kategorii", id);
        try {
            CategoryDto updated = categoryService.updateCategory(id, updateDto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            logger.error("[ADMIN] Błąd aktualizacji kategorii {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategory(@PathVariable Long id) {
        logger.info("[ADMIN] DELETE /admin/categories/{} - usuwanie kategorii", id);
        try {
            categoryService.deleteCategory(id);
            return ResponseEntity.ok(Map.of("message", "Kategoria została usunięta"));
        } catch (RuntimeException e) {
            logger.error("[ADMIN] Błąd usuwania kategorii {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

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