package category.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCategoryDto {
    @Size(min = 2, max = 100, message = "Nazwa kategorii musi mieć od 2 do 100 znaków")
    private String name;

    @Size(max = 500, message = "Opis może mieć maksymalnie 500 znaków")
    private String description;

    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug może zawierać tylko małe litery, cyfry i myślniki")
    private String slug;

    private String imageUrl;

    private Boolean active;

    @Min(value = 0, message = "Kolejność sortowania nie może być ujemna")
    private Integer sortOrder;

    private Long parentId;
}
