package dedeUnivers.dedeUnivers.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import dedeUnivers.dedeUnivers.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import dedeUnivers.dedeUnivers.enums.OrderStatus;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "order_date", nullable = false)
    private Date orderDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;  // Remplacer le String par OrderStatus

    @Column(name = "total_amount", nullable = false)
    private double totalAmount;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany
    @JoinColumn(name = "orderItem_id", nullable = false)
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private Address shippingAddress;

    //    @ManyToMany
//    @JoinTable(name = "order_product", joinColumns = @JoinColumn(name = "order_id"), inverseJoinColumns = @JoinColumn(name = "product_id"))
//    @JsonManagedReference  // Ici, on indique que cette relation doit être sérialisée
//    private Set<Product> products;
    //
    @OneToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    // @ManyToOne
    // @JoinColumn(name = "billing_address_id", nullable = false)
    // private Adress billingAddress;

    // Constructors, getters and setters
    public Order() {
    }

    public Order(int id, Date orderDate, OrderStatus status, double totalAmount) {
        this.id = id;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
    }
}