package dedeUnivers.dedeUnivers.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "quantity", nullable = false)
    @NotNull
    private int quantity;

    @Column(name = "unity_price", nullable = false)
    @NotNull
    private float unityPrice;

    @Column(name = "total_price", nullable = false)
    private double totalPrice; // Prix total (unitPrice * quantity)
//
//    @ManyToOne
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductOption productOption;

    // Constructors, getters and setters

    public OrderItem() {}

    public OrderItem(int id, int quantity, float unityPrice, double totalPrice) {
        this.id = id;
        this.quantity = quantity;
        this.unityPrice = unityPrice;
        this.totalPrice = totalPrice;
    }
}
