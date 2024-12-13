package dedeUnivers.dedeUnivers.controllers;

import dedeUnivers.dedeUnivers.dto.UserDataDto;
import dedeUnivers.dedeUnivers.entities.User;
import dedeUnivers.dedeUnivers.securities.JwtService;
import dedeUnivers.dedeUnivers.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/protected")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;


    @GetMapping("/userdata")
    public ResponseEntity<?> getUserData(HttpServletRequest request) {
        String token = getJwtFromCookies(request);

        if (token == null || jwtService.tokenExpired(token)) {
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.UNAUTHORIZED);
        }

        String email = jwtService.extractUsernameByToken(token);
        Optional<User> userOptional = userService.getUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Exclure des informations sensibles si nécessaire (par exemple, mot de passe, etc.)
            UserDataDto userDataDto = new UserDataDto(user.getId(), user.getFirstname(), user.getLastname(), user.getPseudo(), user.getImageProfil(),user.getEmail(), user.getLoyaltyPoints(), user.getRole().getRoleType().name());
            return new ResponseEntity<>(userDataDto, HttpStatus.OK);
        }

        return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
    }

    private String getJwtFromCookies(HttpServletRequest request) {
        // Extraire le JWT depuis les cookies de la requête
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT".equals(cookie.getName())) {  // Nom du cookie que vous utilisez pour stocker le JWT
                    return cookie.getValue();
                }
            }
        }
        return null;  // Aucun JWT trouvé
    }
}
