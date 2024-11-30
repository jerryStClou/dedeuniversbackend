package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.Material;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Integer> {

    //List<Material> findByProductId(Integer productId);
}

