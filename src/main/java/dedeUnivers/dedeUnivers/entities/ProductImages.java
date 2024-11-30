package dedeUnivers.dedeUnivers.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "productImages")
public class ProductImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "product_images", nullable = false, length = 255)
    @Size(min = 2, max = 255)
    @NotNull
    private String productImages;


    @Column(name = "type_product_images", nullable = false, length = 255)
    @Size(min = 2, max = 255)
    @NotNull
    private String typeProductImages;


    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    public ProductImages() {
    }

    public ProductImages(int id, String productImages, String typeProductImages) {
        this.id = id;
        this.productImages = productImages;
        this.typeProductImages = typeProductImages;
    }


}
