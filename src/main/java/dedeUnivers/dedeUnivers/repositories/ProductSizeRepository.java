package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.ProductSize;

import java.util.List;

public interface ProductSizeRepository   extends JpaRepository<ProductSize, Integer> {

   // List<ProductSize> findByProductId(Integer productId);
}
