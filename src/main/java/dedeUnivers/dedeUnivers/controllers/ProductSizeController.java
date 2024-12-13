package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.ProductSize;
import dedeUnivers.dedeUnivers.services.ProductService;
import dedeUnivers.dedeUnivers.services.ProductSizeService;

import java.util.List;

@RestController
@RequestMapping("api/productSize")
@CrossOrigin(origins = "http://127.0.0.1:3000")
public class ProductSizeController {

    @Autowired
    private ProductSizeService productSizeService;

    @Autowired
    ProductService productService;

    // Méthode pour assainir les entrées utilisateur et éviter les attaques XSS
    private String sanitizeInput(String input) {
        if (input == null) {
            return "";
        }
        // Remplacer les caractères spéciaux pour éviter les attaques XSS
        return input.replaceAll("<", "&lt;").replaceAll(">", "&gt;")
                .replaceAll("&", "&amp;").replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;");
    }

    @GetMapping("/all/{idProduct}")
    public ResponseEntity<List<ProductSize>> getAllProductSizesByProductId(@PathVariable Integer idProduct) {

        try {
            // Assainir les entrées
            String sanitizedIdProduct = sanitizeInput(idProduct.toString());

            List<ProductSize> productSizes = productSizeService.getAllByProductId(Integer.parseInt(sanitizedIdProduct));
            if (productSizes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productSizes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductSize> getProductSizeById(@PathVariable("id") Integer id) {
        try {
            // Assainir l'ID
            String sanitizedId = sanitizeInput(id.toString());

            if (Integer.parseInt(sanitizedId) <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            ProductSize productSize = productSizeService.findById(Integer.parseInt(sanitizedId));
            return new ResponseEntity<>(productSize, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add/{idProduct}")
    public ResponseEntity<ProductSize> addProductSize(@RequestBody ProductSize productSize, @PathVariable Integer idProduct) {
        try {
            if (productSize == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Assainir l'ID du produit
            String sanitizedIdProduct = sanitizeInput(idProduct.toString());

            ProductSize _productSize = productSizeService.addProductSize(productSize, Integer.parseInt(sanitizedIdProduct));
            if (_productSize != null) {
                return new ResponseEntity<>(_productSize, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{idProductSize}")
    public ResponseEntity<String> updateProductSize(@RequestBody ProductSize productSize, @PathVariable Integer idProductSize) {
        try {
            // Assainir l'ID de la taille du produit
            String sanitizedIdProductSize = sanitizeInput(idProductSize.toString());

            ProductSize productSize1 = productSizeService.findById(Integer.parseInt(sanitizedIdProductSize));
            productSize1.setProductSize(productSize.getProductSize());
            productSize1.setInfluenceProductSizePrice(productSize.getInfluenceProductSizePrice());
            productSize1.setInfluenceProductSizeWeight(productSize.getInfluenceProductSizeWeight());

            ProductSize _productSize = productSizeService.save(productSize1);
            if (_productSize != null) {
                return new ResponseEntity<>("Le productSize a pu être modifié !", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> removeColor(@PathVariable("id") Integer id) {

        try {
            // Assainir l'ID
            String sanitizedId = sanitizeInput(id.toString());

            if (Integer.parseInt(sanitizedId) <= 0) {
                return new ResponseEntity<>("Erreur : L'id du productSize à supprimer doit être > 0 !", HttpStatus.BAD_REQUEST);
            }
            productSizeService.remove(Integer.parseInt(sanitizedId));
            return new ResponseEntity<>("Suppression du productSize avec id = '" + sanitizedId + "' effectuée avec succès.", HttpStatus.ACCEPTED);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
