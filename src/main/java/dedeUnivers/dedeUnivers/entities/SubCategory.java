package dedeUnivers.dedeUnivers.entities;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "subCategories")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name_subCategory", nullable = false, length = 100, unique = true)
    @Size(min = 2, max = 60)
    @NotNull
    private String nameSubCategory;


    @Column(name = "image_subCategory", nullable = true, length = 255, unique = false)
    @Size(min = 2, max = 255)
    private String imageSubCategory;


    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Product> products;

    @OneToMany(mappedBy = "subCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<CategorySubCategory> categorySubCategories = new HashSet<>();

    // Constructors
    public SubCategory() {}

    public SubCategory(int id,String nameSubCategory,String imageSubCategory) {
        this.id = id;
        this.nameSubCategory = nameSubCategory;
        this.imageSubCategory = imageSubCategory;
    }

}

