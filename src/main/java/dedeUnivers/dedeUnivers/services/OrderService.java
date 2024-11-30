package dedeUnivers.dedeUnivers.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import dedeUnivers.dedeUnivers.entities.*;
import dedeUnivers.dedeUnivers.enums.PromotionType;
import dedeUnivers.dedeUnivers.projections.MaterialProjection;
import dedeUnivers.dedeUnivers.projections.ProductProjection;
import dedeUnivers.dedeUnivers.projections.ProductSizeProjection;
import dedeUnivers.dedeUnivers.repositories.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    PromotionRepository promotionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProductRepository productRepository;


    /**
     * Calcule le prix total d'un produit en fonction de l'option choisie et de la promotion appliquée.
     *
     * @param productId    L'ID du produit
     * @param productOption L'option de produit sélectionnée (taille + matériau + influence de prix)
     * @param promotion    La promotion appliquée (si applicable)
     * @return Le prix total du produit
     */
    public float calculateProductTotalPrice(Integer productId, ProductOption productOption, Promotion promotion) {
        // Étape 1 : Récupérer le produit
        ProductProjection productProjection = productRepository.findProductById(productId);
        if (productProjection == null) {
            throw new RuntimeException("Produit non trouvé");
        }

        // Étape 2 : Calculer le prix de base du produit
        float basePrice = productProjection.getBasePrice();

        // Étape 3 : Appliquer l'influence de prix du matériau sélectionné
        float materialInfluence = 0.0f;
        if (productOption.getMaterial() != null) {
            materialInfluence = productOption.getMaterial().getInfluenceMaterialPrice();
        }

        // Étape 4 : Appliquer l'influence de prix de la taille sélectionnée
        float sizeInfluence = 0.0f;
        if (productOption.getSize() != null) {
            sizeInfluence = productOption.getSize().getInfluenceProductSizePrice();
        }

        // Étape 5 : Calculer le prix final en fonction des influences
        float finalPrice = basePrice + materialInfluence + sizeInfluence;

        // Étape 6 : Appliquer la promotion si elle est valide
        if (promotion != null && promotion.isValid()) {
            finalPrice = applyPromotion(finalPrice, promotion);
        }

        return finalPrice;
    }

    /**
     * Applique la promotion au prix final du produit.
     *
     * @param price     Le prix de base du produit
     * @param promotion La promotion à appliquer
     * @return Le prix après application de la promotion
     */
    private float applyPromotion(float price, Promotion promotion) {
        // Applique la promotion en fonction de son type
        float finalPrice = price;

        switch (promotion.getType()) {
            case PERCENTAGE:
                finalPrice -= (finalPrice * promotion.getDiscountPercentage() / 100);
                break;
            case CASHBACK:
                finalPrice -= promotion.getDiscountValue();
                break;
            case FIXED_VALUE:
                finalPrice -= promotion.getDiscountValue();
                break;
            case FIRST_PURCHASE:
                // Vérifiez si l'utilisateur est un nouvel acheteur avant d'appliquer
                finalPrice -= promotion.getDiscountValue();
                break;
            case LOYALTY:
                // Appliquez la réduction basée sur les points de fidélité
                finalPrice -= promotion.getDiscountValue();
                break;
        }

        // Assurez-vous que le prix final n'est pas négatif
        return Math.max(finalPrice, 0.0f);
    }



    @Transactional
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

}
