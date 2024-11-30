package dedeUnivers.dedeUnivers.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import dedeUnivers.dedeUnivers.entities.Address;
import dedeUnivers.dedeUnivers.entities.ProductPromotion;
import dedeUnivers.dedeUnivers.entities.Promotion;
import dedeUnivers.dedeUnivers.services.ProductPromotionService;

import java.util.List;

@RestController
@RequestMapping("api/productPromotion")
public class ProductPromotionController {

    @Autowired
    private ProductPromotionService productPromotionService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductPromotion>>getAll(){
        try{
            List<ProductPromotion> productPromotions = productPromotionService.getAll();
            return new ResponseEntity<>(productPromotions, HttpStatus.ACCEPTED);
        }catch (RuntimeException e){

        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    //
    @PostMapping("/add/{idProduct}/{idPromotion}")
    public ResponseEntity<ProductPromotion> addProductPromotion(@PathVariable("idProduct") Integer idProduct,  @PathVariable("idPromotion") Integer idPromotion){
        try {
            ProductPromotion productPromotion1 = productPromotionService.addProductPromotion(idProduct,idPromotion);
            return new ResponseEntity<>(productPromotion1, HttpStatus.CREATED);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    // Modifier une promotion
    @PutMapping("/update/{productPromotionId}/{idProduct}/{idPromotion}")
    public ResponseEntity<String> updateProductPromotion(@PathVariable("productPromotionId")  Integer productPromotionId,@PathVariable("idProduct") Integer idProduct,  @PathVariable("idPromotion") Integer idPromotion) {
        try {
            ProductPromotion productPromotion = productPromotionService.updateProductPromotion(productPromotionId,idProduct,idPromotion);
            return new ResponseEntity<>("Sa marche !!", HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // Promotion non trouvée
        }
    }


    // Supprimer une promotion
    @DeleteMapping("/remove/{productPromotionId}")
    public ResponseEntity<String> deleteProductPromotion(@PathVariable("productPromotionId")  Integer productPromotionId){
        try {
            productPromotionService.deleteProductPromotion(productPromotionId);
            return new ResponseEntity<String>("Suppression du lien entre produit et promotion avec id = '" + productPromotionId + "' effectuée avec succès.",
                    HttpStatus.ACCEPTED);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

    }

}

