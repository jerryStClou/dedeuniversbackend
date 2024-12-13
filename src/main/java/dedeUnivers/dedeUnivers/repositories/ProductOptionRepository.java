package dedeUnivers.dedeUnivers.repositories;

import dedeUnivers.dedeUnivers.entities.Color;
import dedeUnivers.dedeUnivers.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.ProductOption;
import dedeUnivers.dedeUnivers.entities.ProductSize;

import java.util.List;

public interface ProductOptionRepository   extends JpaRepository<ProductOption, Integer> {
    List<ProductOption> findByProductId(Integer productId);
    List<Color> findColorsByProductId(Integer productId);

    // Récupérer tous les matériaux associés à un produit par son ID
    List<Material> findMaterialsByProductId(Integer productId);

    // Récupérer toutes les tailles associées à un produit par son ID
    List<ProductSize> findSizesByProductId(Integer productId);


    List<ProductOption> findByColor_Id(int colorId);


    List<ProductOption> findByMaterial_Id(int colorId);


    List<ProductOption> findBySize_Id(int colorId);


}

