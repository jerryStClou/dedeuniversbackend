package dedeUnivers.dedeUnivers.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "total_amount", nullable = false)
    private double totalAmount; // Montant total du panier

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany
    @JoinColumn(name = "cartItem_id", nullable = false)
    private Set<CartItem> cartItems;



    public Cart() {}

    public Cart(int id) {
        this.id = id;
    }


    // Méthode pour ajouter un produit au panier
    public void addProductToCart(CartItem cartItem) {
        this.cartItems.add(cartItem);
        this.totalAmount += cartItem.getTotalPrice(); // Mise à jour du total du panier
    }

    // Méthode pour supprimer un produit du panier
    public void removeProductFromCart(CartItem cartItem) {
        this.cartItems.remove(cartItem);
        this.totalAmount -= cartItem.getTotalPrice(); // Mise à jour du total du panier
    }

}
