package dedeUnivers.dedeUnivers.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dedeUnivers.dedeUnivers.entities.User;
import dedeUnivers.dedeUnivers.projections.ValidationCodeProjection;

//import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Méthode pour trouver un utilisateur par son email
    Optional<User> findByEmail(String email);
}