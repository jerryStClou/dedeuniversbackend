package dedeUnivers.dedeUnivers.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "material", nullable = false, length = 100, unique = true)
    @Size(min = 2, max = 100)
    @NotNull
    private String material;
    //
    @Column(name = "influence_material_price", nullable = false)
    private float influenceMaterialPrice;

    @Column(name = "influence_material_weight", nullable = false)
    private float influenceMaterialWeight;

//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;

    public Material() {
    }

    public Material(int id, String material, float influenceMaterialPrice, float influenceMaterialWeight) {
        this.id = id;
        this.material = material;
        this.influenceMaterialPrice = influenceMaterialPrice;
        this.influenceMaterialWeight = influenceMaterialWeight;
    }
}