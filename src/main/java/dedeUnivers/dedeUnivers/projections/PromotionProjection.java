package dedeUnivers.dedeUnivers.projections;

import dedeUnivers.dedeUnivers.enums.PromotionType;
import dedeUnivers.dedeUnivers.enums.PromotionType;

import java.util.Date;

public interface PromotionProjection {

    // L'ID de la promotion
    Integer getId();

    // Le code de la promotion
    String getCode();

    // La description de la promotion
    String getDescription();

    // Le pourcentage de réduction (si applicable)
    Float getDiscountPercentage();

    // La valeur de réduction (si applicable)
    Float getDiscountValue();

    // La limite d'utilisation de la promotion
    Integer getUsageLimit();

    // Le nombre actuel d'utilisations de la promotion
    Integer getUsageCount();

    // Indiquer si la promotion est réservée au premier achat
    Boolean getIsFirstPurchaseOnly();

    // Le type de la promotion (par exemple, pourcentage ou montant fixe)
    PromotionType getType();

    // Les points de fidélité requis pour la promotion (si applicable)
    Integer getRequiredLoyaltyPoints();

    // La date de début de validité de la promotion
    Date getStartDate();

    // La date de fin de validité de la promotion
    Date getEndDate();
}


