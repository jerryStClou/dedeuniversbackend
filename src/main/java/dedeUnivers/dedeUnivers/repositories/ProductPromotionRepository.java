package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import dedeUnivers.dedeUnivers.entities.ProductPromotion;

import java.util.Set;

public interface ProductPromotionRepository  extends JpaRepository<ProductPromotion, Integer> {

    @Query("SELECT pp FROM ProductPromotion pp WHERE pp.promotion.id = :promotionId")
    Set<ProductPromotion> findByPromotionId(@Param("promotionId") Integer promotionId);
}
