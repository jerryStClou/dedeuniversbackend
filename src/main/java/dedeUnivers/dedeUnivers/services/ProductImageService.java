package dedeUnivers.dedeUnivers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.Cart;
import dedeUnivers.dedeUnivers.entities.Comment;
import dedeUnivers.dedeUnivers.entities.ProductImages;
import dedeUnivers.dedeUnivers.repositories.ProductImagesRepository;

import java.util.List;
import java.util.Set;

@Service
public class ProductImageService {

    @Autowired
    private ProductImagesRepository productImageRepository;

    public ProductImages save(ProductImages productImage) {
        return productImageRepository.save(productImage);
    }

    public ProductImages findById(Integer id) {
        return productImageRepository.findById(id).get();
    }


    public List<ProductImages> getAllByProductId(Integer productId) {
        return productImageRepository.findByProductId(productId);
    }

    public Set<ProductImages> getProductImagesByIdSubCategory(Integer idSubCategory){
        return  productImageRepository.findBySubCategoryId(idSubCategory);
    }

    public List<ProductImages> getAllProductImage() {
        return productImageRepository.findAll();
    }

    public void remove(Integer id) {
        productImageRepository.deleteById(id);
    }

    public List<ProductImages> getTop4ProductImagesByProductIdAndType(int productId) {
        // Crée un Pageable pour limiter les résultats à 4 images
        Pageable pageable = PageRequest.of(0, 5); // 0 : première page, 4 : taille de la page
        return productImageRepository.findTop4ByProductIdAndTypeProductImages(productId, "card", pageable);
    }

}
