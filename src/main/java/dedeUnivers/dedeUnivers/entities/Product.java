package dedeUnivers.dedeUnivers.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name_product", nullable = false, length = 100)
    @Size(min = 2, max = 100)
    @NotNull
    private String nameProduct;


    @Column(name = "description", nullable = false, length = 255)
    @Size(max = 255)
    private String description;

    @Column(name = "stock", nullable = false)
    @Min(0)
    @NotNull
    private int stock;

    @Column(name = "base_price", nullable = false)
    private float basePrice;

    @Column(name = "base_weight", nullable = false)
    private float baseWeight;

    @ManyToOne
    @JoinColumn(name = "subCategory_id", nullable = false)
    private SubCategory subCategory;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ProductImages> productImages;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Comment> comments;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ProductOption> productOptions;
//
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<ProductPromotion> productPromotions;


    // Constructors
    public Product() {}

    public Product(int id, String nameProduct, int stock, String description) {
        this.id = id;
        this.nameProduct = nameProduct;
        this.stock = stock;
        this.description = description;
    }
}
