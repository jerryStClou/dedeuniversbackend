package dedeUnivers.dedeUnivers.services;

import dedeUnivers.dedeUnivers.entities.*;
import dedeUnivers.dedeUnivers.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.repositories.ProductOptionRepository;
import dedeUnivers.dedeUnivers.repositories.ProductSizeRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductSizeService {

    @Autowired
    private ProductSizeRepository productSizeRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductRepository productRepository;


    public ProductSize save(ProductSize productSize) {
        return productSizeRepository.save(productSize);
    }

    public ProductSize findById(Integer id) {
        return productSizeRepository.findById(id).get();
    }

    public List<ProductSize> getAllProductSize() {
        return productSizeRepository.findAll();
    }

    public List<ProductSize> getAllByProductId(Integer productId) {

        // Récupérer le produit par son id
        Optional<Product> productOptional = productRepository.findById(productId);

        // Si le produit existe
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Récupérer les ProductOption associées au produit
            Set<ProductOption> productOptions = product.getProductOptions();

            // Extraire les ProductSize de chaque ProductOption
            List<ProductSize> productSizes = productOptions.stream()
                    .map(ProductOption::getSize)  // Récupérer le ProductSize de chaque ProductOption
                    .filter(Objects::nonNull)  // Filtrer les ProductOption sans ProductSize
                    .collect(Collectors.toList());

            return productSizes;
        }

        // Si le produit n'est pas trouvé, retourner une liste vide ou une erreur
        return Collections.emptyList();

    }

    public void remove(Integer id) {
       List<ProductOption> productOptions = productOptionRepository.findBySize_Id(id);

        for (ProductOption productOption : productOptions) {
            productOption.setSize(null);
        }

        productSizeRepository.deleteById(id);
    }



    public ProductSize addProductSize(ProductSize productSize, Integer idProduct){
       // ProductOption productOption = productOptionRepository.findById(idProductOption).get();
        ProductSize productSize1 = productSizeRepository.save(productSize);
        List<ProductOption> productOptions = productOptionRepository.findByProductId(idProduct);
        for (ProductOption productOption:productOptions){
            productOption.setSize(productSize1);
            productOptionRepository.save(productOption);
        }

        return productSize1;
    }

    public List<ProductSize> getAllSizeByProductId(Integer idProduct){
        return productOptionRepository.findSizesByProductId(idProduct);
    }



}
