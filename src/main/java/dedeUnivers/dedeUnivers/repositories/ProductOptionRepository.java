package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.ProductOption;
import dedeUnivers.dedeUnivers.entities.ProductSize;

import java.util.List;

public interface ProductOptionRepository   extends JpaRepository<ProductOption, Integer> {
    List<ProductOption> findByProductId(Integer productId);
}

