package dedeUnivers.dedeUnivers.securities;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.servlet.http.Cookie;



public class CustomCsrfCookieFilter extends OncePerRequestFilter {

    // Implémentation correcte de la méthode doFilterInternal
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        // Récupérer le token CSRF depuis la requête
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (csrfToken != null) {
            // Créer un cookie pour le token CSRF
            Cookie csrfCookie = new Cookie("XSRF-TOKEN", csrfToken.getToken());
            csrfCookie.setPath("/");  // Définir le chemin du cookie
            csrfCookie.setHttpOnly(true);  // Empêcher l'accès JavaScript
            csrfCookie.setSecure(true);  // S'assurer que le cookie est sécurisé (seulement sur HTTPS)

            // Ajouter l'attribut SameSite dans l'en-tête Set-Cookie
            String cookieHeader = String.format("%s=%s; Path=%s; HttpOnly; Secure; SameSite=Lax",
                    csrfCookie.getName(),
                    csrfCookie.getValue(),
                    csrfCookie.getPath());

            // Ajouter l'en-tête Set-Cookie pour envoyer le cookie CSRF au client
            //response.addHeader("Set-Cookie", cookieHeader);
            response.setHeader("X-CSRF-TOKEN", csrfToken.getToken()); // Ajoutez le token dans l'en-tête
        }

        // Continuer la chaîne de filtres
        chain.doFilter(request, response);
    }
}
