package dedeUnivers.dedeUnivers.projections;

import java.util.Set;

public interface ProductProjection {
    Integer getId();
    String getNameProduct();
    String getDescription();
    Integer getStock();
    Float getBasePrice();
    Float getBaseWeight();

    SubCategoryProjection getSubCategory();
    Set<ProductImagesProjection> getProductImages();
    Set<CommentProjection> getComments();
    //Set<ProductPromotionProjection> getProductPromotions();
   Set<ProductOptionProjection> getProductOptions();
}

