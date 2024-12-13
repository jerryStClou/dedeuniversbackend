package dedeUnivers.dedeUnivers.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:3000")  // Frontend URL
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Permettre les méthodes HTTP nécessaires
//                .allowedHeaders("*")  // Permet tous les en-têtes
//                .allowCredentials(true);  // Permet les cookies et les en-têtes d'authentification
//    }
}
