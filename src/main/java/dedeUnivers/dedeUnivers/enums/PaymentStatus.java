package dedeUnivers.dedeUnivers.enums;

public enum PaymentStatus {
    PENDING("En attente"),      // Paiement en attente
    COMPLETED("Terminé"),       // Paiement complété
    FAILED("Échoué"),           // Paiement échoué
    REFUNDED("Remboursé");      // Paiement remboursé

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
