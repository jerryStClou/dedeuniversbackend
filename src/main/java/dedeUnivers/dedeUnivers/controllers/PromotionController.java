package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Promotion;
import dedeUnivers.dedeUnivers.services.PromotionService;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

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
    public ResponseEntity<List<Promotion>> getAllPromotion() {
        try {
            List<Promotion> promotions = promotionService.getAllPromotion();
            return new ResponseEntity<>(promotions, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotion(@PathVariable("id") Integer id) {
        try {
            // Assainir l'ID
            String sanitizedId = sanitizeInput(id.toString());

            Promotion promotion = promotionService.getPromotion(Integer.parseInt(sanitizedId));
            return new ResponseEntity<>(promotion, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/add")
    public ResponseEntity<Promotion> addPromotion(@RequestBody Promotion promotion) {
        try {
            if (promotion == null) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }

            // Assainir les champs sensibles de l'objet Promotion
            promotion.setCode(sanitizeInput(promotion.getCode()));
            promotion.setDescription(sanitizeInput(promotion.getDescription()));

            Promotion createdPromotion = promotionService.addPromotion(promotion);
            return new ResponseEntity<>(createdPromotion, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PutMapping("/update/{promotionId}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable int promotionId, @RequestBody Promotion updatedPromotion) {
        try {
            // Assainir l'ID de la promotion
            String sanitizedPromotionId = sanitizeInput(String.valueOf(promotionId));

            // Assainir les champs sensibles de l'objet Promotion
            updatedPromotion.setCode(sanitizeInput(updatedPromotion.getCode()));
            updatedPromotion.setDescription(sanitizeInput(updatedPromotion.getDescription()));

            Promotion promotion = promotionService.updatePromotion(Integer.parseInt(sanitizedPromotionId), updatedPromotion);
            return new ResponseEntity<>(promotion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Promotion non trouvée
        }
    }

    @DeleteMapping("/remove/{promotionId}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Integer promotionId) {
        try {
            // Assainir l'ID de la promotion
            String sanitizedPromotionId = sanitizeInput(promotionId.toString());

            promotionService.deletePromotion(Integer.parseInt(sanitizedPromotionId));
            return new ResponseEntity<>(HttpStatus.OK); // Suppression réussie
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Promotion non trouvée
        }
    }
}
