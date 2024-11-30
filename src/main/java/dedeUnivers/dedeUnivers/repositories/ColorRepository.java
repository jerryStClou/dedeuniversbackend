package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.Color;

import java.util.List;

public interface ColorRepository   extends JpaRepository<Color, Integer> {
//
//    List<Color> findByProductId(Integer productId);
}

