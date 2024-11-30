package dedeUnivers.dedeUnivers.enums;

public enum OrderStatus {
    PENDING("En attente"),      // Commande en attente
    PAID("Payée"),              // Commande payée
    SHIPPED("Expédiée"),        // Commande expédiée
    DELIVERED("Livrée"),        // Commande livrée
    CANCELLED("Annulée");       // Commande annulée

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
