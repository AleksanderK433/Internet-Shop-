package category.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class CategoryDto {
    private Long id;
    private String name;
    private String description;
    private String slug;
    private String imageUrl;
    private Boolean active;
    private Integer sortOrder;
    private Long parentId;
    private String parentName;
    private Integer level;
    private String fullPath;
    private Boolean hasChildren;
    private Boolean hasProducts;
    private Integer productCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private List<CategoryDto> children;
}
