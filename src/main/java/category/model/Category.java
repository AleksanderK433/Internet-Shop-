package category.model;

import com.shop.internetshop.product.model.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nazwa kategorii jest wymagana")
    @Size(min = 2, max = 100, message = "Nazwa kategorii musi mieć od 2 do 100 znaków")
    @Column(nullable = false)
    private String name;

    @Size(max = 500, message = "Opis może mieć maksymalnie 500 znaków")
    @Column(columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Slug jest wymagany")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug może zawierać tylko małe litery, cyfry i myślniki")
    @Column(unique = true, nullable = false)
    private String slug;  // SEO-friendly URL

    @Column(name = "image_url")
    private String imageUrl;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "sort_order")
    private Integer sortOrder = 0;  // Dla sortowania kategorii

    // Hierarchia kategorii - parent/child relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> children = new ArrayList<>();

    // Relacja z produktami
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Konstruktory
    public Category() {}

    public Category(String name, String slug) {
        this.name = name;
        this.slug = slug;
    }

    public Category(String name, String slug, Category parent) {
        this.name = name;
        this.slug = slug;
        this.parent = parent;
    }

    // Helper methods
    public boolean isRoot() {
        return parent == null;
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public boolean hasProducts() {
        return products != null && !products.isEmpty();
    }

    public int getLevel() {
        int level = 0;
        Category current = this.parent;
        while (current != null) {
            level++;
            current = current.getParent();
        }
        return level;
    }

    public String getFullPath() {
        if (parent == null) {
            return name;
        }
        return parent.getFullPath() + " > " + name;
    }

    // ToString bez produktów żeby uniknąć recursive calls
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", slug='" + slug + '\'' +
                ", active=" + active +
                ", level=" + getLevel() +
                '}';
    }
}
