package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.Order;

public interface OrderRepository  extends JpaRepository<Order,Integer> {
}

