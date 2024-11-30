package dedeUnivers.dedeUnivers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.Cart;
import dedeUnivers.dedeUnivers.entities.ProductOption;
import dedeUnivers.dedeUnivers.repositories.CartRepository;
import dedeUnivers.dedeUnivers.repositories.ProductOptionRepository;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }

    public Cart findById(Integer id) {
        return cartRepository.findById(id).get();
    }

    public List<Cart> getAllCart() {
        return cartRepository.findAll();
    }

    public void remove(Integer id) {
        cartRepository.deleteById(id);
    }
}
