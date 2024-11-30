package dedeUnivers.dedeUnivers.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cartItems")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "price", nullable = false)
    private float price;


    @Column(name = "total_price", nullable = false)
    private double totalPrice; // Prix total (unitPrice * quantity)

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public CartItem() {}

    public CartItem(int id, int quantity, float price, double totalPrice) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }
}
