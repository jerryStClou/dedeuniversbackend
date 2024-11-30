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


    @GetMapping("/all")
    public ResponseEntity<List<Promotion>> getAllPromotion(){
        try{
            List<Promotion> promotions = promotionService.getAllPromotion();
            return new ResponseEntity<>(promotions, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Promotion> getPromotion(@PathVariable("id")  Integer id){
        try{
            Promotion promotion = promotionService.getPromotion(id);
            return new ResponseEntity<>(promotion, HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Ajouter une promotion
    @PostMapping("/add")
    public ResponseEntity<Promotion> addPromotion(@RequestBody Promotion promotion) {
        Promotion createdPromotion = promotionService.addPromotion(promotion);
        return new ResponseEntity<>(createdPromotion, HttpStatus.CREATED);
    }

    // Modifier une promotion
    @PutMapping("/update/{promotionId}")
    public ResponseEntity<Promotion> updatePromotion(@PathVariable int promotionId, @RequestBody Promotion updatedPromotion) {
        try {
            Promotion promotion = promotionService.updatePromotion(promotionId, updatedPromotion);
            return new ResponseEntity<>(promotion, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Promotion non trouvée
        }
    }

    // Supprimer une promotion
    @DeleteMapping("/remove/{promotionId}")
    public ResponseEntity<Void> deletePromotion(@PathVariable Integer promotionId) {
        try {
            promotionService.deletePromotion(promotionId);
            return new ResponseEntity<>(HttpStatus.OK); // Suppression réussie
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Promotion non trouvée
        }
    }
}

