package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import dedeUnivers.dedeUnivers.entities.Comment;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.SubCategory;
import dedeUnivers.dedeUnivers.projections.ProductProjection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ProductRepository  extends JpaRepository<Product, Integer> {
    List<Product> findByNameProductContaining(String nameProduct);

    List<Product> findBySubCategoryOrderBySubCategory_IdAsc(SubCategory subCategory);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImages")
    Set<Product> findAllProductsWithImages();

    @EntityGraph(attributePaths = {"productImages", "productSizes", "colors", "materials", "comments"})
    @Query("SELECT p FROM Product p")
    Set<Product> findAllProductsWithAssociations();

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImages WHERE p.subCategory.nameSubCategory = :nameSubCategory")
    Set<Product> findBySubCategory_NameSubCategoryWithImages(@Param("nameSubCategory") String nameSubCategory);


    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.productImages WHERE p.subCategory.id = :subCategoryId")
    Set<Product> findBySubCategoryIdWithImages(@Param("subCategoryId") Integer subCategoryId);


    // List<Product> findBySubCategoryId(Integer subCategoryId);

    @Query("SELECT p FROM Product p JOIN FETCH p.productImages WHERE p.subCategory.id = :subCategoryId")
    List<ProductProjection> findBySubCategoryId(Integer subCategoryId);




//
//    @Query("SELECT p.id AS id, p.name AS nameProduct, p.description AS description, p.stock AS stock, p.basePrice AS basePrice, p.baseWeight AS baseWeight, " +
//            "p.subCategory AS subCategory, p.productImages AS productImages, p.comments AS comments, p.colors AS colors, p.materials AS materials, " +
//            "p.productSizes AS productSizes " +
//            "FROM Product p WHERE p.id = :id")
//    ProductProjection findProductProjectionById(@Param("id") Integer id);


    ProductProjection findProductById(Integer id);

//
//        @Query("SELECT p  FROM Product WHERE p.id = :id")
//        ProductProjection findProductProjectionById(@Param("id") Integer id);



//
//    @Query("SELECT p.id AS id, p.name AS nameProduct, p.description AS description, p.stock AS stock, p.basePrice AS basePrice, p.baseWeight AS baseWeight, " +
//            "p.subCategory AS subCategory, p.productImages AS productImages, p.comments AS comments, p.colors AS colors, p.materials AS materials, " +
//            "p.productSizes AS productSizes " +
//            "FROM Product p JOIN p.subCategory s WHERE s.nameSubCategory = :nameSubCategory")
//    List<ProductProjection> findProductsBySubCategoryName(@Param("nameSubCategory") String nameSubCategory);

//
//    @Query("SELECT p.id AS id, p.name AS nameProduct, p.description AS description, p.stock AS stock, p.basePrice AS basePrice, p.baseWeight AS baseWeight, " +
//            "p.subCategory AS subCategory, p.productImages AS productImages, p.comments AS comments, p.colors AS colors, p.materials AS materials, " +
//            "p.productSizes AS productSizes " +
//            "FROM Product p JOIN p.subCategory s WHERE s.id = :subCategoryId")
//    List<ProductProjection> findTop10BySubCategoryId(@Param("subCategoryId") Integer subCategoryId, Pageable pageable);
//

    List<ProductProjection> findBySubCategoryId(Integer subCategoryId, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN p.subCategory sc WHERE sc.nameSubCategory = :nameSubCategory")
    List<ProductProjection> findBySubCategoryName(@Param("nameSubCategory") String nameSubCategory);

    List<ProductProjection> findBySubCategoryId(int subCategoryId);

//
//    @EntityGraph(attributePaths = {"productImages", "productSizes", "colors", "materials"})
//    Optional<Product> findById(Integer id);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.subCategory.id = :subCategoryId")
    Integer countBySubCategoryId(Integer subCategoryId);

    List<Product> findByNameProductOrderByIdAsc(String nameProduct);

    //List<Product> findByPriceOrderByIdAsc(float price);

    List<Product> findByNameProductOrderByIdDesc(String nameProduct);

    //List<Product> findByPriceOrderByIdDesc(float price);

    Product findByNameProduct(String nameProduct);

    List<Product> findBySubCategory_IdOrderByProductImagesAsc(Integer id);


    @Query("SELECT p FROM Product p " +
            "JOIN FETCH p.productImages pi " +
            "WHERE p.subCategory.id = :subCategoryId " +
            "AND (pi.typeProductImages = 'small carousel')")
    List<Product> findProductsBySubCategoryAndImageType(@Param("subCategoryId") int subCategoryId);

    List<Product> findBySubCategoryId(int subCategoryId, Pageable pageable);

}



