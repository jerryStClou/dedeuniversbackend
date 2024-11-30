package dedeUnivers.dedeUnivers.entities;

import dedeUnivers.dedeUnivers.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import dedeUnivers.dedeUnivers.enums.PaymentStatus;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "amount", nullable = false)
    @NotNull
    private float amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @NotNull
    private PaymentStatus status;

    @Column(name = "payment_date", nullable = false)
    @NotNull
    private Date paymentDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Constructors, getters, and setters
    public Payment() {}

    public Payment(int id, float amount, PaymentStatus status, Date paymentDate) {
        this.id = id;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
    }
}

