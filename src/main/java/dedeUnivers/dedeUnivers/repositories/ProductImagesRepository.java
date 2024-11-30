package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import dedeUnivers.dedeUnivers.entities.ProductImages;

import java.util.List;
import java.util.Set;

public interface ProductImagesRepository extends JpaRepository<ProductImages, Integer> {

    List<ProductImages> findByProductId(Integer productId);

    @Query("SELECT pi FROM ProductImages pi JOIN pi.product p WHERE p.subCategory.id = :subCategoryId")
    Set<ProductImages> findBySubCategoryId(@Param("subCategoryId") Integer subCategoryId);

    // Requête personnalisée pour récupérer 4 images de produit par ID et type d'image "card"
    @Query("SELECT pi FROM ProductImages pi WHERE pi.product.id = :productId AND pi.typeProductImages = :typeProductImages")
    List<ProductImages> findTop4ByProductIdAndTypeProductImages(
            @Param("productId") int productId,
            @Param("typeProductImages") String typeProductImages,
            Pageable pageable);

}

