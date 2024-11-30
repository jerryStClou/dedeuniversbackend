package dedeUnivers.dedeUnivers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.ProductPromotion;
import dedeUnivers.dedeUnivers.entities.Promotion;
import dedeUnivers.dedeUnivers.projections.ProductProjection;
import dedeUnivers.dedeUnivers.repositories.ProductPromotionRepository;
import dedeUnivers.dedeUnivers.repositories.ProductRepository;
import dedeUnivers.dedeUnivers.repositories.PromotionRepository;

import java.util.List;

@Service
public class ProductPromotionService {

    @Autowired
    private ProductPromotionRepository productPromotionRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    public List<ProductPromotion> getAll(){
        return productPromotionRepository.findAll();
    }

    public ProductPromotion addProductPromotion(Integer idProduct, Integer idPromotion){
        Product product = productRepository.findById(idProduct).get();
        Promotion promotion = promotionRepository.findById(idPromotion).get();
        ProductPromotion productPromotion = new ProductPromotion();
        productPromotion.setProduct(product);
        productPromotion.setPromotion(promotion);
        return productPromotionRepository.save(productPromotion);
    }

    public ProductPromotion updateProductPromotion(Integer idProductPromotion,Integer idProduct, Integer idPromotion){
        Product product = productRepository.findById(idProduct).get();
        Promotion promotion = promotionRepository.findById(idPromotion).get();
        ProductPromotion productPromotion = productPromotionRepository.findById(idProductPromotion).get();
        productPromotion.setPromotion(promotion);
        productPromotion.setProduct(product);
        return productPromotionRepository.save(productPromotion);
    }

    public void deleteProductPromotion(Integer idProductPromotion){
        productPromotionRepository.deleteById(idProductPromotion);
    }
}

