package dedeUnivers.dedeUnivers.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "productOptions")
public class ProductOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
//
//    @Column(name = "price_influence", nullable = false)
//    private float priceInfluence;
//
//    @Column(name = "weight_influence", nullable = false)
//    private float weightInfluence;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "size_id", nullable = true)
    private ProductSize size;
    //
    @ManyToOne
    @JoinColumn(name = "color_id", nullable = true)
    private Color color;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = true)
    private Material material;

    public ProductOption() {
    }

    public ProductOption(int id) {
        this.id = id;

    }
}

