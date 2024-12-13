package dedeUnivers.dedeUnivers.securities;

import dedeUnivers.dedeUnivers.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.GET;


//@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);

        return authenticationManagerBuilder.build();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Configuration CSRF
        http.csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())// Utilisation d'un cookie pour le token CSRF
                        //.csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler()) // Ajouter le gestionnaire pour les SPA
                        //.csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
                        .ignoringRequestMatchers(
                                "/api/auth/login", // Ignorer CSRF pour la route login
                                "/api/auth/register", // Ignorer CSRF pour la route register
                                "/api/auth/activation", // Ignorer CSRF pour la route activation
                                "/api/auth/generate/validation-code", // Ignorer CSRF pour la génération de code
                                "/api/auth/new-password", // Ignorer CSRF pour la route de réinitialisation du mot de passe
                                "/api/auth/change-password", // Ignorer CSRF pour la route de changement de mot de passe
                                "/api/auth/logout" // Ignorer CSRF pour la déconnexion
                        )
                )
                // Ajouter le filtre personnalisé pour les cookies CSRF
                .addFilterAfter(new CustomCsrfCookieFilter(), CsrfFilter.class)
                .addFilterBefore((request, response, chain) -> {
                    CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                    System.out.println("Token CSRF attendu (Spring): " + token.getToken());
                    chain.doFilter(request, response);
                }, UsernamePasswordAuthenticationFilter.class)

                // Configuration des autorisations des requêtes
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/activation",
                                "/api/auth/generate/validation-code",
                                "/api/auth/new-password",
                                "/api/auth/change-password",
                                "/api/auth/logout"
                        ).permitAll() // Permet l'accès sans authentification pour ces routes

                        .requestMatchers(
                                "api/product/all/{idSubCategory}",
                                "api/product/{id}",
                                "api/product/products/subcategory/{id}",
                                "api/product/productProjection/{id}",
                                "api/product/products/subCategory/{id}/top10",
                                "api/product/products/by-subcategory/{nameSubCategory}",
                                "api/product/images/{subCategoryId}",
                                "api/product/top4/{subCategoryId}"
                        ).permitAll() // Permet l'accès sans authentification pour ces routes

                        .requestMatchers(GET, "api/product/products/by-subcategory/{nameSubCategory}").permitAll() // Permet l'accès GET à cette route

                        .requestMatchers(
                                "api/productImage/all/{idProduct}",
                                "api/productImage/byIdsubCategory/{idSubCategory}",
                                "api/productImage/product/{productId}/card"
                        ).permitAll() // Permet l'accès sans authentification pour ces routes

                        // Règles pour les routes protégées par le rôle ADMIN
                        .requestMatchers(
                                "api/product/add/{idSubCategory}",
                                "api/product/update/{idProduct}",
                                "api/product/remove/{id}",
                                "api/product/all",
                                "/api/category/**",
                                "api/color/**",
                                "api/material/**",
                                "api/productSize/**",
                                "api/subCategory/**"
                        ).hasRole("ADMIN") // Seul l'ADMIN peut accéder à ces routes

                        // Autres routes nécessitant le rôle CLIENT
                        .requestMatchers(
                                "api/address/**",
                                "api/comment/all/user/{idUser}",
                                "api/comment/update/{idComment}",
                                "api/comment/remove/{id}"
                        ).hasRole("CLIENT") // Seul le CLIENT peut accéder à ces routes

                        // Toute autre requête doit être authentifiée
                        .anyRequest().authenticated()
                )

                // Ajouter le filtre JWT avant le filtre UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Configuration de la gestion de la déconnexion
                .logout(logout -> logout
                        .logoutUrl("/logout") // Définir l'URL de déconnexion
                        .invalidateHttpSession(true) // Invalider la session
                        .clearAuthentication(true) // Effacer l'authentification
                        .deleteCookies("JWT", "refreshToken") // Supprimer les cookies JWT et refreshToken à la déconnexion
                )

                // Configuration CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        // Retourne la configuration du SecurityFilterChain
        return http.build();
    }


    // Configuration CORS spécifique

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("X-XSRF-TOKEN", "Content-Type", "Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}


