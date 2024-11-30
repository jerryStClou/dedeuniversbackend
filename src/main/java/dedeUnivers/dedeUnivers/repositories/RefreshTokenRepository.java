package dedeUnivers.dedeUnivers.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dedeUnivers.dedeUnivers.entities.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByRefreshToken(String token);
}

