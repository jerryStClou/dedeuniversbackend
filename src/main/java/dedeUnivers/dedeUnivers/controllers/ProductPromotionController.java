package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.ProductPromotion;
import dedeUnivers.dedeUnivers.services.ProductPromotionService;

import java.util.List;

@RestController
@RequestMapping("api/productPromotion")
public class ProductPromotionController {

    @Autowired
    private ProductPromotionService productPromotionService;

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

    @GetMapping("/all")
    public ResponseEntity<List<ProductPromotion>> getAll() {
        try {
            List<ProductPromotion> productPromotions = productPromotionService.getAll();
            return new ResponseEntity<>(productPromotions, HttpStatus.ACCEPTED);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/add/{idProduct}/{idPromotion}")
    public ResponseEntity<ProductPromotion> addProductPromotion(@PathVariable("idProduct") Integer idProduct,
                                                                @PathVariable("idPromotion") Integer idPromotion) {
        try {
            // Assainir les entrées
            String sanitizedIdProduct = sanitizeInput(idProduct.toString());
            String sanitizedIdPromotion = sanitizeInput(idPromotion.toString());

            // Conversion après assainissement
            ProductPromotion productPromotion1 = productPromotionService.addProductPromotion(
                    Integer.parseInt(sanitizedIdProduct), Integer.parseInt(sanitizedIdPromotion));
            return new ResponseEntity<>(productPromotion1, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{productPromotionId}/{idProduct}/{idPromotion}")
    public ResponseEntity<String> updateProductPromotion(@PathVariable("productPromotionId") Integer productPromotionId,
                                                         @PathVariable("idProduct") Integer idProduct,
                                                         @PathVariable("idPromotion") Integer idPromotion) {
        try {
            // Assainir les entrées
            String sanitizedProductPromotionId = sanitizeInput(productPromotionId.toString());
            String sanitizedIdProduct = sanitizeInput(idProduct.toString());
            String sanitizedIdPromotion = sanitizeInput(idPromotion.toString());

            // Conversion après assainissement
            productPromotionService.updateProductPromotion(
                    Integer.parseInt(sanitizedProductPromotionId),
                    Integer.parseInt(sanitizedIdProduct),
                    Integer.parseInt(sanitizedIdPromotion));
            return new ResponseEntity<>("Sa marche !!", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Promotion non trouvée
        }
    }

    @DeleteMapping("/remove/{productPromotionId}")
    public ResponseEntity<String> deleteProductPromotion(@PathVariable("productPromotionId") Integer productPromotionId) {
        try {
            // Assainir l'ID du productPromotion
            String sanitizedProductPromotionId = sanitizeInput(productPromotionId.toString());

            productPromotionService.deleteProductPromotion(Integer.parseInt(sanitizedProductPromotionId));
            return new ResponseEntity<>("Suppression du lien entre produit et promotion avec id = '" + sanitizedProductPromotionId + "' effectuée avec succès.",
                    HttpStatus.ACCEPTED);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
