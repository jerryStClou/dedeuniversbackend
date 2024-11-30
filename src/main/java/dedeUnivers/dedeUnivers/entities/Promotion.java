package dedeUnivers.dedeUnivers.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import dedeUnivers.dedeUnivers.enums.PromotionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import dedeUnivers.dedeUnivers.enums.PromotionType;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "promotions")
public class Promotion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code", nullable = false, unique = true)
    @NotNull
    private String code;

    @Column(name = "description")
    private String description; // Description de la promotion "Réduction de 10% sur le premier achat"
//

    @Column(name = "discount_percentage")
    private Float discountPercentage; // Pourcentage de réduction (si applicable)

    @Column(name = "discount_value")
    private Float discountValue; // Valeur de réduction (si applicable)

    @Column(name = "usage_limit")
    private int usageLimit; // Limite d'utilisation pour cette promotion (ex. 1000 fois)

    @Column(name = "usage_count")
    private int usageCount;  // Nombre d'utilisations actuelles de la promotion

    @Column(name = "is_first_purchase_only")
    private boolean firstPurchaseOnly; // Si la promotion est réservée au premier achat uniquement

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_type", nullable = false)
    private PromotionType type; // Type de la promotion (ex. pourcentage, montant fixe, etc.)

    @Column(name = "required_loyalty_points")
    private Integer requiredLoyaltyPoints; // Points de fidélité requis pour cette promotion (si applicable)

    @Column(name = "start_date", nullable = false)
    @NotNull
    private Date startDate; // Date de début de validité de la promotion

    @Column(name = "end_date", nullable = false)
    @NotNull
    private Date endDate; // Date de fin de validité de la promotion


    // Constructors
    public Promotion() {}

    public Promotion(int id, String code, String description, Float discountPercentage, Float discountValue, int usageLimit, int usageCount, boolean firstPurchaseOnly, PromotionType type, Integer requiredLoyaltyPoints, Date startDate, Date endDate) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.discountPercentage = discountPercentage;
        this.discountValue = discountValue;
        this.usageLimit = usageLimit;
        this.usageCount = usageCount;
        this.firstPurchaseOnly = firstPurchaseOnly;
        this.type = type;
        this.requiredLoyaltyPoints = requiredLoyaltyPoints;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Méthode pour vérifier si la promotion est toujours valide (optionnelle, à utiliser dans la logique métier)
    public boolean isValid() {
        Date currentDate = new Date();
        return currentDate.after(startDate) && currentDate.before(endDate);
    }

    // Méthode pour vérifier si la promotion a atteint son nombre d'utilisation maximum (optionnelle)
    public boolean isUsageLimitReached() {
        return usageCount >= usageLimit;
    }


}

