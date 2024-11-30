package dedeUnivers.dedeUnivers.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.Product;
import dedeUnivers.dedeUnivers.entities.ProductPromotion;
import dedeUnivers.dedeUnivers.entities.Promotion;
import dedeUnivers.dedeUnivers.repositories.ProductPromotionRepository;
import dedeUnivers.dedeUnivers.repositories.ProductRepository;
import dedeUnivers.dedeUnivers.repositories.PromotionRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ProductPromotionRepository productPromotionRepository;

    @Autowired
    private ProductRepository productRepository;

    public Promotion getPromotion(Integer idPromotion){
        return promotionRepository.findById(idPromotion).get();
    }

    public List<Promotion> getAllPromotion(){
        return promotionRepository.findAll();
    }

    // Ajouter une promotion
    public Promotion addPromotion(Promotion promotion) {
        return  promotionRepository.save(promotion);
    }

    // Modifier une promotion existante
    public Promotion updatePromotion(int promotionId, Promotion updatedPromotion) {
        // Vérifier si la promotion existe avant de la modifier
        Optional<Promotion> existingPromotion = promotionRepository.findById(promotionId);
        if (existingPromotion.isPresent()) {
            Promotion promotion = existingPromotion.get();
            // Mettre à jour les propriétés de la promotion existante
            promotion.setCode(updatedPromotion.getCode());
            promotion.setDescription(updatedPromotion.getDescription());
            promotion.setDiscountPercentage(updatedPromotion.getDiscountPercentage());
            promotion.setStartDate(updatedPromotion.getStartDate());
            promotion.setEndDate(updatedPromotion.getEndDate());
            promotion.setUsageLimit(updatedPromotion.getUsageLimit());
            promotion.setUsageCount(updatedPromotion.getUsageCount());
            promotion.setFirstPurchaseOnly(updatedPromotion.isFirstPurchaseOnly());
            promotion.setType(updatedPromotion.getType());

            return promotionRepository.save(promotion); // Sauvegarder les modifications
        } else {
            throw new RuntimeException("Promotion not found with id: " + promotionId);
        }
    }

    // Supprimer une promotion
    public void deletePromotion(Integer promotionId) {
//        Optional<Promotion> existingPromotion = promotionRepository.findById(promotionId);
//        if (existingPromotion.isPresent()) {
//           // Set<ProductPromotion> productPromotions = productPromotionRepository.findByPromotionId(promotionId);
//            //productPromotionRepository.deleteAll(productPromotions);
//            promotionRepository.deleteById(promotionId);
//        } else {
//            throw new RuntimeException("Promotion not found with id: " + promotionId);
//        }

        System.out.println("Tentative de suppression de la promotion avec ID: " + promotionId);
        Set<ProductPromotion> productPromotions = productPromotionRepository.findByPromotionId(promotionId);
        System.out.println("Relations produit-promotion trouvées: " + productPromotions.size());
        productPromotionRepository.deleteAll(productPromotions);
        promotionRepository.deleteById(promotionId);
        System.out.println("Promotion supprimée");
    }
}