package dedeUnivers.dedeUnivers.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dedeUnivers.dedeUnivers.entities.Payment;

public interface PaymentRepository   extends JpaRepository<Payment, Integer> {
}
