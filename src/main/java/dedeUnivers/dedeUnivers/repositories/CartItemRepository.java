package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.CartItem;

public interface CartItemRepository   extends JpaRepository<CartItem, Integer> {
}

