package dedeUnivers.dedeUnivers.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "productSizes")
public class ProductSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "product_size", nullable = false, length = 100, unique = true)
    @Size(min = 2, max = 100)
    @NotNull
    private String productSize;

    @Column(name = "influence_productSize_price", nullable = false)
    private float influenceProductSizePrice;

    @Column(name = "influence_productSize_weight", nullable = false)
    private float influenceProductSizeWeight;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;


    // Constructors
    public ProductSize() {}

    public ProductSize(String productSize, int id, float influenceProductSizePrice, float influenceProductSizeWeight) {
        this.productSize = productSize;
        this.id = id;
        this.influenceProductSizePrice = influenceProductSizePrice;
        this.influenceProductSizeWeight = influenceProductSizeWeight;
    }
}
