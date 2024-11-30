package dedeUnivers.dedeUnivers.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@Entity
@Table(name = "colors")
public class Color {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "product_color", nullable = false, length = 100, unique = true)
    @Size(min = 2, max = 100)
    @NotNull
    private String color;
//
//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;

    // Constructors
    public Color() {}

    public Color(String color) {
        this.color = color;
    }

}