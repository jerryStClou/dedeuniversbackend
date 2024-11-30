package dedeUnivers.dedeUnivers.securities;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtLogoutFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Supposons que le token JWT est stocké dans un cookie ou un en-tête
        String jwtToken = getJwtFromRequest(request);

        if (jwtToken != null) {
            // Logique de déconnexion : vous pouvez invalider le token côté serveur ou le supprimer du côté client
            // Ici, nous allons juste supprimer le cookie contenant le token JWT
            clearJwtToken(response);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        // Logic pour extraire le JWT de l'en-tête ou des cookies
        return request.getHeader("Authorization");  // Par exemple, récupérer le token depuis l'en-tête Authorization
    }

    private void clearJwtToken(HttpServletResponse response) {
        // Exemple de suppression du cookie contenant le JWT
        Cookie cookie = new Cookie("JWT", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");  // Définir le chemin du cookie
        response.addCookie(cookie);
    }
}

