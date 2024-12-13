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
        // Récupérer le token depuis le cookie
        String jwtToken = getJwtFromRequest(request);

        if (jwtToken != null) {
            // Logique de déconnexion : vous pouvez invalider le token côté serveur ou le supprimer du côté client
            // Ici, nous allons juste supprimer le cookie contenant le token JWT
            clearJwtToken(response);
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        // Récupérer le token depuis les cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("JWT".equals(cookie.getName())) {
                    return cookie.getValue();  // Retourner le JWT du cookie
                }
            }
        }
        return null;  // Retourne null si aucun cookie JWT n'est trouvé
    }

    private void clearJwtToken(HttpServletResponse response) {
        // Exemple de suppression du cookie contenant le JWT
        Cookie cookie = new Cookie("JWT", null);
        cookie.setMaxAge(0);  // Expirer immédiatement
        cookie.setPath("/");  // Définir le chemin du cookie
        response.addCookie(cookie);
    }
}

