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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        // Configuration CORS directement dans la méthode securityFilterChain
        http
                .csrf(csrf -> csrf.disable())  // Désactivation de la protection CSRF
                .authorizeHttpRequests(authorize -> authorize
                        //.requestMatchers("/api/comment/add/{idProduct}/{idUser}").authenticated()  // Protège cette route
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/activation", "/api/auth/generate/validation-code", "/api/auth/new-password", "/api/auth/change-password")
                        .permitAll()
                        .requestMatchers("api/product/all/{idSubCategory}","api/product/{id}","api/product/products/subcategory/{id}","api/product/productProjection/{id}","api/product/products/subCategory/{id}/top10","api/product/products/by-subcategory/{nameSubCategory}","api/product/images/{subCategoryId}","api/product/top4/{subCategoryId}","api/comment/all/product/{idProduct}")
                        .permitAll()
                        .requestMatchers(GET,"api/product/products/by-subcategory/{nameSubCategory}").permitAll()
                        .requestMatchers("api/productImage/all/{idProduct}","api/productImage/byIdsubCategory/{idSubCategory}","api/productImage/product/{productId}/card").permitAll()
                        .requestMatchers("api/product/add/{idSubCategory}","api/product/update/{idProduct}","api/product/remove/{id}","api/product/all")
                        .hasRole("ADMIN")
                        .requestMatchers("/api/category/**","api/color/**","api/material/**","api/productSize/**","api/subCategory/**").hasRole("ADMIN")
                        .requestMatchers("api/productOption/**","api/productPromotion/**","/api/promotions/**","api/subCategory/**").hasRole("ADMIN")
                        .requestMatchers("api/productImage/add/{idProduct}","api/productImage/update/{idProductImages}","api/productImage/remove/{id}").hasRole("ADMIN")
                        .requestMatchers("api/address/**","api/comment/all/user/{idUser}","api/comment/update/{idComment}","api/comment/remove/{id}").hasRole("CLIENT")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                );

        // Ajout de la configuration CORS
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    // Configuration CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:3000");  // Frontend URL
        corsConfig.addAllowedMethod("*");  // Permet toutes les méthodes HTTP
        corsConfig.addAllowedHeader("*");  // Permet tous les en-têtes
        corsConfig.setAllowCredentials(true);  // Permet les cookies et les en-têtes d'authentification

        // Création de la source de configuration CORS
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return source;
    }
}

