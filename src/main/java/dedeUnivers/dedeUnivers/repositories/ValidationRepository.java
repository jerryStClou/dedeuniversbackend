package dedeUnivers.dedeUnivers.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dedeUnivers.dedeUnivers.entities.Validation;

public interface ValidationRepository  extends JpaRepository<Validation, Integer> {
    Optional<Validation> findByValidationCode(String validationCode);
    Optional<Validation> findByEmail(String email);

}
