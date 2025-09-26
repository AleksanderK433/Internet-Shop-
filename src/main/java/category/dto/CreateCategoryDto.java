package category.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryDto {
    @NotBlank(message = "Nazwa kategorii jest wymagana")
    @Size(min = 2, max = 100, message = "Nazwa kategorii musi mieć od 2 do 100 znaków")
    private String name;

    @Size(max = 500, message = "Opis może mieć maksymalnie 500 znaków")
    private String description;

    @NotBlank(message = "Slug jest wymagany")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug może zawierać tylko małe litery, cyfry i myślniki")
    private String slug;

    private String imageUrl;

    private Boolean active = true;

    @Min(value = 0, message = "Kolejność sortowania nie może być ujemna")
    private Integer sortOrder = 0;

    private Long parentId;
}