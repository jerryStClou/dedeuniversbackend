package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.Promotion;

public interface PromotionRepository   extends JpaRepository<Promotion, Integer> {
}

