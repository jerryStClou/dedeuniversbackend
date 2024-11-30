package dedeUnivers.dedeUnivers.projections;

public interface ProductOptionProjection {
    Integer getId();
//    Float getPriceInfluence();
//    Float getWeightInfluence();
    //ProductProjection getProduct();
    MaterialProjection getMaterial();
    ProductSizeProjection getSize();
    ColorProjection getColor();
}

