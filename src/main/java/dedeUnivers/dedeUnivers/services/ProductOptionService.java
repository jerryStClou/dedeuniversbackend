package dedeUnivers.dedeUnivers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.Category;
import dedeUnivers.dedeUnivers.entities.Color;
import dedeUnivers.dedeUnivers.entities.ProductOption;
import dedeUnivers.dedeUnivers.repositories.CategoryRepository;
import dedeUnivers.dedeUnivers.repositories.ProductOptionRepository;

import java.util.List;

@Service
public class ProductOptionService {

    @Autowired
    private ProductOptionRepository productOptionRepository;

    public ProductOption save(ProductOption productOption) {
        return productOptionRepository.save(productOption);
    }

    public ProductOption findById(Integer id) {
        return productOptionRepository.findById(id).get();
    }


    public List<ProductOption> getAllByProductId(Integer productId) {
        return productOptionRepository.findByProductId(productId);
    }

    public List<ProductOption> getAllProductOption() {
        return productOptionRepository.findAll();
    }

    public void remove(Integer id) {
        productOptionRepository.deleteById(id);
    }
}

