package dedeUnivers.dedeUnivers.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.Color;
import dedeUnivers.dedeUnivers.entities.Material;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.ProductOption;
import dedeUnivers.dedeUnivers.repositories.MaterialRepository;
import dedeUnivers.dedeUnivers.repositories.ProductOptionRepository;
import dedeUnivers.dedeUnivers.repositories.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductOptionRepository productOptionRepository;

    public Material save(Material material) {
        return materialRepository.save(material);
    }

    public Material findById(Integer id) {
        return materialRepository.findById(id).get();
    }

    public List<Material> getAllMaterial() {
        return materialRepository.findAll();
    }

    public List<Material> getAllByProductId(Integer productId) {

        // Récupérer le produit par son id
        Optional<Product> productOptional = productRepository.findById(productId);

        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            // Récupérer les ProductOption associées au produit
            Set<ProductOption> productOptions = product.getProductOptions();

            // Extraire les Materials de chaque ProductOption
            List<Material> materials = productOptions.stream()
                    .map(ProductOption::getMaterial) // Récupérer le Material de chaque ProductOption
                    .filter(Objects::nonNull) // Filtrer les ProductOption sans Material
                    .collect(Collectors.toList());

            return materials;
        }

        // Si le produit n'existe pas, retourner une liste vide ou lever une exception
        return Collections.emptyList();
    }

    public void remove(Integer id) {
        List<ProductOption> productOptions = productOptionRepository.findByMaterial_Id(id);
        // Supprimer chaque ProductOption individuellement
        for (ProductOption productOption : productOptions) {
            productOption.setMaterial(null);
        }
        materialRepository.deleteById(id);
    }


    public Material addMaterial(Material material, Integer idProduct){
        //ProductOption productOption = productOptionRepository.findById(idProductOption).get();
        Material material1 = materialRepository.save(material);
        List<ProductOption> productOptions = productOptionRepository.findByProductId(idProduct);
        for (ProductOption productOption:productOptions){
            productOption.setMaterial(material1);
            productOptionRepository.save(productOption);
        }
        return material1;
    }

    public List<Material> getAllMaterialByProductId(Integer idProduct){
        return productOptionRepository.findMaterialsByProductId(idProduct);
    }

}
