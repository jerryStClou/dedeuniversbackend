package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.Cart;

public interface CartRepository   extends JpaRepository<Cart, Integer> {
}

