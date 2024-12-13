package dedeUnivers.dedeUnivers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.Color;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.ProductOption;
import dedeUnivers.dedeUnivers.repositories.ColorRepository;
import dedeUnivers.dedeUnivers.repositories.ProductOptionRepository;
import dedeUnivers.dedeUnivers.repositories.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ColorService {

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductRepository productRepository;

    public Color save(Color color) {
        return colorRepository.save(color);
    }

    public Color findById(Integer id) {
        return colorRepository.findById(id).get();
    }

    public List<Color> getAllColor() {
        return colorRepository.findAll();
    }

//    //public List<Color> getAllByProductId(Integer productId) {
//        return colorRepository.findByProductId(productId);
//    }


    public void remove(Integer id) {
        List<ProductOption> productOptions = productOptionRepository.findByColor_Id(id);
        // Supprimer chaque ProductOption individuellement
        for (ProductOption productOption : productOptions) {
            productOption.setColor(null);
        }

        colorRepository.deleteById(id);
    }


    public Color addColor(Color color, Integer idProduct){
       // ProductOption productOption = productOptionRepository.findById(idProduct).get();
        List<ProductOption> productOptions = productOptionRepository.findByProductId(idProduct);

        Color color1 = colorRepository.save(color);
        for (ProductOption productOption:productOptions){
            productOption.setColor(color1);
            productOptionRepository.save(productOption);
        }

        return color1;
    }

    public List<Color> getAllColorByProductId(Integer idProduct){
        //List<Color> colors = productOptionRepository.findColorsByProductId(idProduct);
        //return colors;

        // Récupérer le produit par son id
        Optional<Product> productOptional = productRepository.findById(idProduct);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Récupérer les ProductOption associées au produit
            Set<ProductOption> productOptions = product.getProductOptions();

            // Extraire les Materials de chaque ProductOption
            List<Color> colors = productOptions.stream()
                    .map(ProductOption::getColor) // Récupérer le Material de chaque ProductOption
                    .filter(Objects::nonNull) // Filtrer les ProductOption sans Material
                    .collect(Collectors.toList());

            return colors;
        }

        // Si le produit n'existe pas, retourner une liste vide ou lever une exception
        return Collections.emptyList();

    }

}
