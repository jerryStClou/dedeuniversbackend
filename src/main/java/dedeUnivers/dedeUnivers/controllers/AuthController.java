package dedeUnivers.dedeUnivers.controllers;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import dedeUnivers.dedeUnivers.dto.*;
import dedeUnivers.dedeUnivers.entities.*;
import dedeUnivers.dedeUnivers.repositories.JwtRepository;
import dedeUnivers.dedeUnivers.repositories.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import dedeUnivers.dedeUnivers.securities.JwtService;
import dedeUnivers.dedeUnivers.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("api/auth")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    JwtRepository jwtRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;


    // Map pour stocker les tentatives de connexion par adresse IP
    private final Map<String, LoginAttempt> ipAttempts = new ConcurrentHashMap<>();

    // Configuration de la limite de tentatives (5 tentatives en 10 minutes)
    private static final int MAX_ATTEMPTS = 5;
    private static final long LOCK_TIME_DURATION = Duration.ofMinutes(10).toMillis(); // 10 minutes

    private static final long ATTEMPT_WINDOW = Duration.ofMinutes(10).toMillis(); // 10 minutes

    @PostMapping("/generate/validation-code")
    public ResponseEntity<String> generatenerValidationCode(@RequestBody EmailVerificationDto emailVerificationDto) {
        try {
            String validationCode = userService.generateValidationCode();
            //userService.sendValidationEmail(email, validationCode);
            userService.saveValidationCode(validationCode,emailVerificationDto.getEmail());
            return new ResponseEntity<>("the code has been sent to the address "+emailVerificationDto.getEmail(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("error while generating code", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/activation")
    public ResponseEntity<String> activation(@RequestBody ValidationCodeDto validationCodeDto) {
        try {
            Optional<Validation> validationOpt = userService.getValidationByCode(validationCodeDto.getCode());

            if (validationOpt.isPresent()) {
                Validation validation = validationOpt.get();
                if (validation.getValidationCode().equals(validationCodeDto.getCode())
                    && LocalDateTime.now().isBefore(validation.getValidationCodeExpiry())) {
                    validation.setActivation(true);
                    userService.saveValidation(validation);
                    return new ResponseEntity<>("Le code de validation est correct, l'utilisateur a été enregistré avec succès", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("Le code de validation est incorrect ou expiré", HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>("Le code de validation n'a pas été trouvé", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Échec de l'activation de l'utilisateur", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDto userRegistrationDto) {
        try {
            // Essayer d'enregistrer l'utilisateur et retourner la réponse
            String message = userService.register(userRegistrationDto);
            return new ResponseEntity<>(message, HttpStatus.CREATED);  // 201 CREATED est plus approprié ici
        } catch (IllegalArgumentException e) {
            // Si une exception est lancée dans le service, on retourne BAD_REQUEST
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Si une autre exception se produit, on retourne un 500
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
//
////    @PostMapping("/login")
////    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginDto loginDto) {
////        try {
////            // Authentifier l'utilisateur
////            Authentication authentication = authenticationManager.authenticate(
////                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
////            );
////
////            // Si l'authentification est réussie, générer un JWT
////            User user = (User) authentication.getPrincipal();
////            Map<String, String> tokens = jwtService.generateJwtRefreshToken(user);
////
////            return new ResponseEntity<>(tokens, HttpStatus.OK);
////        } catch (BadCredentialsException e) {
////            return new ResponseEntity<>(Map.of("message", "Invalid credentials"), HttpStatus.UNAUTHORIZED);
////        } catch (Exception e) {
////            log.error("Error during login: ", e);
////            return new ResponseEntity<>(Map.of("message", "Login failed"), HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////    }
//
//
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto) {
//        try {
//             //Authentifier l'utilisateur
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
//            );
//
//            // Si l'authentification est réussie, générer un JWT
//            //User user = (User) authentication.getPrincipal();
//            Optional<User> userOptional = userService.getUserByEmail(loginDto.getUsername());
//            User user = null;
//            if(userOptional.isPresent()){
//                user = userOptional.get();
//            }
//            Map<String, String> tokens = jwtService.generateJwtRefreshToken(user);
//            return new ResponseEntity<>(tokens, HttpStatus.OK);
//        } catch (BadCredentialsException e) {
//            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
//        } catch (Exception e) {
//            log.error("Error during login: ", e);
//            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto loginDto, HttpServletResponse response, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();
        LoginAttempt loginAttempt = ipAttempts.computeIfAbsent(ipAddress, key -> new LoginAttempt());

        // Vérifier si l'adresse IP est bloquée
        if (loginAttempt.isBlocked()) {
            long remainingTime = (loginAttempt.getBlockedUntil() - System.currentTimeMillis()) / 1000;
            return new ResponseEntity<>("Too many login attempts. Please try again in " + remainingTime + " seconds.", HttpStatus.TOO_MANY_REQUESTS);
        }

        // Authentifier l'utilisateur
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );

            // Si l'authentification réussit, générer les tokens
            Optional<User> userOptional = userService.getUserByEmail(loginDto.getUsername());
            User user = userOptional.orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

            Map<String, String> tokens = jwtService.generateJwtRefreshToken(user);

            // Créer et ajouter les cookies de JWT et de refresh token
            String jwtToken = tokens.get("jwtToken");
            String refreshToken = tokens.get("refreshToken");

            Cookie jwtCookie = new Cookie("JWT", jwtToken);
            jwtCookie.setHttpOnly(true);  // Le cookie est accessible uniquement via HTTP (pas de JavaScript)
            jwtCookie.setSecure(true);    // Assurez-vous que cette option est activée en production avec HTTPS
            jwtCookie.setPath("/");      // Le cookie est accessible sur tout le site
            jwtCookie.setMaxAge(3600);   // Le token expire après 1 heure
            response.addCookie(jwtCookie);

            Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
            refreshCookie.setHttpOnly(true);
            refreshCookie.setSecure(true);
            refreshCookie.setPath("/");
            refreshCookie.setMaxAge(7 * 24 * 60 * 60);  // Le refresh token est valide pendant 7 jours
            response.addCookie(refreshCookie);

            // Réinitialiser le compteur de tentatives en cas de succès
            loginAttempt.reset();

            return new ResponseEntity<>(tokens, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            log.error("Authentication failed for user: {}", loginDto.getUsername(), e);

            // Augmenter le compteur de tentatives après une tentative échouée
            loginAttempt.incrementFailedAttempts();
            if (loginAttempt.getFailedAttempts() >= MAX_ATTEMPTS) {
                // Bloquer l'IP si le nombre de tentatives échouées dépasse la limite
                loginAttempt.block();
            }

            return new ResponseEntity<>("Invalid username or password", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Error during login: ", e);
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        try {
            // Supprimer les cookies en les réinitialisant à null et en les envoyant avec un âge de 0
            Cookie jwtCookie = new Cookie("JWT", null);
            jwtCookie.setMaxAge(0);  // Expirer immédiatement
            jwtCookie.setPath("/");  // Le chemin doit être le même que lors de la création du cookie
            response.addCookie(jwtCookie);

            Cookie refreshCookie = new Cookie("refreshToken", null);
            refreshCookie.setMaxAge(0);
            refreshCookie.setPath("/");
            response.addCookie(refreshCookie);

            // Retourner une réponse de succès
            return new ResponseEntity<>("Déconnexion réussie", HttpStatus.OK);
        } catch (Exception e) {
            log.error("Erreur lors de la déconnexion : ", e);
            return new ResponseEntity<>("Erreur lors de la déconnexion", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        // Récupérer le refreshToken depuis le cookie
        Cookie[] cookies = request.getCookies();
        String refreshTokenCookie = null;
        for (Cookie cookie : cookies) {
            if ("refreshToken".equals(cookie.getName())) {
                refreshTokenCookie = cookie.getValue();
                break;
            }
        }

        if (refreshTokenCookie == null) {
            return new ResponseEntity<>("Refresh token non trouvé dans les cookies.", HttpStatus.BAD_REQUEST);
        }

        // Vérifier si le refresh token est valide
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByRefreshToken(refreshTokenCookie);
        if (optionalRefreshToken.isEmpty()) {
            return new ResponseEntity<>("Refresh token invalide.", HttpStatus.UNAUTHORIZED);
        }

        RefreshToken refreshToken = optionalRefreshToken.get();

        // Vérifier si le refresh token est expiré ou désactivé
        if (refreshToken.isExpired() || refreshToken.isDisabled()) {
            return new ResponseEntity<>("Refresh token expiré ou désactivé.", HttpStatus.UNAUTHORIZED);
        }

        // Trouver l'utilisateur associé à ce refresh token
        User user = refreshToken.getUser();
        if (user == null) {
            return new ResponseEntity<>("Utilisateur non trouvé.", HttpStatus.NOT_FOUND);
        }

        // Vérifier si le JWT actuel est valide pour l'utilisateur
        Optional<Jwt> optionalJwt = jwtRepository.findTopByUserOrderByCreatedAtDesc(user); // Trouver le dernier JWT valide

        // Si le JWT n'existe pas ou n'est pas valide, retourner une erreur
        Jwt jwt = optionalJwt.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "JWT non trouvé pour l'utilisateur"));

        // Vérifier si le JWT actuel est expiré
        if (jwtService.tokenExpired(jwt.getToken())) {
            // Si le JWT est expiré, vous pouvez renvoyer une erreur ou en générer un nouveau
            return new ResponseEntity<>("Le token est expiré, veuillez vous reconnecter", HttpStatus.UNAUTHORIZED);
        }
        // Générer un nouveau JWT et un nouveau refresh token
        Map<String, String> tokens = jwtService.generateJwtRefreshToken(user);

        // Créer et ajouter les nouveaux cookies
        Cookie jwtCookie = new Cookie("JWT", tokens.get("jwtToken"));
        jwtCookie.setHttpOnly(true);  // Sécuriser le cookie
        jwtCookie.setSecure(true);    // Utiliser uniquement sur HTTPS
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(3600);    // Expire dans 1 heure
        response.addCookie(jwtCookie);

        Cookie refreshCookie = new Cookie("refreshToken", tokens.get("refreshToken"));
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // Le refresh token reste valide pendant 7 jours
        response.addCookie(refreshCookie);

        // Retourner les nouveaux tokens dans la réponse
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }


//
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
//        try {
//            // Vérifier si l'en-tête Authorization est présent et commence par "Bearer "
//            return new ResponseEntity<>(authorizationHeader, HttpStatus.OK);
//        } catch (Exception e) {
//            // En cas d'erreur (token invalide, etc.)
//            log.error("Erreur lors de la déconnexion : ", e);
//            return new ResponseEntity<>("Erreur lors de la déconnexion.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



    @PostMapping("/new-password")
    public ResponseEntity<String> newPassword(@RequestBody String email) {
        try {
            String mdp = userService.newPassword(email);
            return new ResponseEntity<>(mdp, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to generate new password", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        try {
            boolean isChanged = userService.changePassword(
                changePasswordDto.getEmail(),
                changePasswordDto.getOldPassword(),
                changePasswordDto.getNewPassword()
            );
            if (isChanged) {
                return new ResponseEntity<>("Password changed successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Old password is incorrect", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to change password", HttpStatus.BAD_REQUEST);
        }
    }


    // Classe interne pour gérer les tentatives de connexion
    private static class LoginAttempt {
        private int failedAttempts = 0;
        private long lastAttemptTime = 0;
        private long blockedUntil = 0;

        // Incrémenter le nombre de tentatives échouées
        public void incrementFailedAttempts() {
            failedAttempts++;
            lastAttemptTime = System.currentTimeMillis();
        }

        // Réinitialiser le compteur
        public void reset() {
            failedAttempts = 0;
            lastAttemptTime = 0;
            blockedUntil = 0;
        }

        // Vérifier si l'IP est bloquée
        public boolean isBlocked() {
            if (failedAttempts >= MAX_ATTEMPTS && (System.currentTimeMillis() - lastAttemptTime) < ATTEMPT_WINDOW) {
                return true;
            } else if (System.currentTimeMillis() > blockedUntil) {
                reset(); // Si le temps de blocage est écoulé, réinitialiser
                return false;
            }
            return false;
        }

        // Bloquer l'IP pendant un certain temps
        public void block() {
            blockedUntil = System.currentTimeMillis() + LOCK_TIME_DURATION; // Bloquer pour 10 minutes
        }

        public int getFailedAttempts() {
            return failedAttempts;
        }

        public long getBlockedUntil() {
            return blockedUntil;
        }
    }

}
