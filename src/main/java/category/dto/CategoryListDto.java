package category.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryListDto {
    private Long id;
    private String name;
    private String slug;
    private Integer level;
    private String fullPath;
    private Boolean hasProducts;
    private Integer productCount;
}
