package dedeUnivers.dedeUnivers.repositories;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import dedeUnivers.dedeUnivers.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import dedeUnivers.dedeUnivers.entities.Jwt;

public interface JwtRepository extends JpaRepository<Jwt, Integer>  {
    Optional<Jwt> findByTokenAndDisabledAndExpired(String token, boolean disabled, boolean expired);

    Optional<Jwt> findByUserEmailAndDisabledAndExpired(String email, boolean disabled, boolean expired);

    Optional<Jwt> findByRefreshTokenRefreshToken(String token);

    Stream<Jwt> findByUserEmail(String email);
    
    Optional<Jwt> findByToken(String token);

    Optional<Jwt> findTopByUserOrderByCreatedAtDesc(User user);

}
