package dedeUnivers.dedeUnivers.securities;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import dedeUnivers.dedeUnivers.entities.Jwt;
import dedeUnivers.dedeUnivers.entities.RefreshToken;
import dedeUnivers.dedeUnivers.entities.User;
import dedeUnivers.dedeUnivers.repositories.JwtRepository;
import dedeUnivers.dedeUnivers.repositories.UserRepository;
import dedeUnivers.dedeUnivers.repositories.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@AllArgsConstructor
@Service
public class JwtService {


    private final String secretKey;

    // Assurez-vous que le constructeur est annoté avec @Autowired si nécessaire
    @Autowired
    public JwtService(@Value("${jwt.secret}") String secretKey) {
        this.secretKey = secretKey;
    }


    @Autowired
    private JwtRepository jwtRepository;

    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private final long EXPIRATION_TIME = 30 * 60 * 1000;


    // Generate a key for signing the JWT using BASE64 decoding
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract claims from the token using a custom function
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    
    private Date getExpirationDateFromToken(String token) {
        return this.getClaim(token, Claims::getExpiration);
    }

    
    public boolean tokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }
    
    // Method to get expiration date from token by name using getClaim
    public Date getExpirationDateByToken(String token) {
        return getClaim(token, Claims::getExpiration);
    }


    
    // Extract username from token using getClaim
    public String extractUsernameByToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    
    // Generate a new JWT token with a 30-minute validity period and set claims
//    public String generateJwtToken(User user) {
//        Date now = new Date();
//        Date expiryDate = new Date(now.getTime() + 1800000); // Token valid for 30 minutes
//
//        Map<String, Object> claims = new HashMap<>();
//        claims.put(Claims.SUBJECT, user.getEmail());
//        claims.put("lastname", user.getLastname());
//        claims.put("firstname", user.getFirstname());
//        claims.put(Claims.EXPIRATION, expiryDate);
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(user.getEmail())
//                .setIssuedAt(now)
//                .setExpiration(expiryDate)
//                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
//                .compact();
//    }

    public String generateJwtToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 36000000); // Token valid for 30 minutes

        Map<String, Object> claims = new HashMap<>();
        claims.put("lastname", user.getLastname());
        claims.put("firstname", user.getFirstname());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getEmail())  // Assurez-vous que vous utilisez la bonne valeur pour le sujet
                .setIssuedAt(now)
                .setExpiration(expiryDate) // L'expiration est déjà définie ici
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    // Get Jwt by token
    public Optional<Jwt> getJwtToken(String token) {
        return jwtRepository.findByToken(token);
    }


    // Générer et insérer un Jwt dans la base de données
    public Jwt generateJwt(User user) {
        String token = generateJwtToken(user);
        Jwt jwt = new Jwt();
        jwt.setToken(token);
        jwt.setDisabled(false);
        jwt.setExpired(false);
        jwt.setCreatedAt(LocalDateTime.now());
        jwt.setExpiresAt(LocalDateTime.now().plusMinutes(30 * 10 * 60)); // 30 minutes de validité
        jwt.setUser(user);
        
        return jwtRepository.save(jwt);
    }


    
    // Méthode pour rafraîchir le token en utilisant le refresh token
    public String refreshToken(String refreshTokenStr) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByRefreshToken(refreshTokenStr);

        if (refreshTokenOpt.isPresent()) {
            RefreshToken refreshToken = refreshTokenOpt.get();

            if (refreshToken.isExpired() || refreshToken.isDisabled()) {
                throw new RuntimeException("Refresh token is invalid.");
            }

            // Générer un nouveau token JWT
            User user = refreshToken.getUser();
            String newToken = generateJwtToken(user);

            // Optionnel: Marquer le refresh token comme désactivé et expiré
            refreshToken.setDisabled(true);
            refreshToken.setExpired(true);
            refreshTokenRepository.save(refreshToken);

            return newToken;
        } else {
            throw new RuntimeException("Refresh token not found.");
        }
    }


    
    // Générer un refresh token et l'insérer dans la base de données
    public RefreshToken generateRefreshToken(User user) {
        String token = UUID.randomUUID().toString(); // Générer un token unique
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setRefreshToken(token);
        refreshToken.setDisabled(false);
        refreshToken.setExpired(false);
        refreshToken.setCreatedAt(LocalDateTime.now());
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(7)); // Refresh token valable pendant 7 jours
        refreshToken.setUser(user);
        
        return refreshTokenRepository.save(refreshToken);
    }


    // Générer à la fois un jwtToken et un refreshToken
    public Map<String, String> generateJwtRefreshToken(User user) {
        RefreshToken refreshToken = generateRefreshToken(user);
        //String jwtToken = generateJwtToken(user);
        Jwt jwt = generateJwt(user);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("jwtToken", jwt.getToken());
        tokens.put("refreshToken", refreshToken.getRefreshToken());

        return tokens;
    }

    
    // Méthode de déconnexion pour invalider le JWT
    public void logout(String token) {
        Optional<Jwt> jwtOpt = jwtRepository.findByToken(token);
        if (jwtOpt.isPresent()) {
            Jwt jwt = jwtOpt.get();
            jwt.setDisabled(true);
            jwt.setExpired(true);
            jwtRepository.save(jwt);
        } else {
            throw new RuntimeException("Token not found.");
        }
    }

    public boolean isValid(String token, UserDetails userDetails) {
        // Extraire le nom d'utilisateur (subject) du token
        String usernameFromToken = extractUsernameByToken(token);

        // Vérifier si le token n'est pas expiré
        if (tokenExpired(token)) {
            return false; // Le token est expiré
        }

        // Vérifier si le nom d'utilisateur extrait du token correspond au nom d'utilisateur de l'utilisateur authentifié
        return usernameFromToken.equals(userDetails.getUsername());
    }



}
