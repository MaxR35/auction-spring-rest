package fr.rougeux.projet.auction.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration Spring MVC pour la gestion du CORS (Cross-Origin Resource Sharing).
 * <p>
 * Cette classe permet de définir les règles d’accès entre le backend Spring Boot
 * et le frontend Angular (ou toute autre application web externe).
 * </p>
 *
 * <h3>Objectif :</h3>
 * <ul>
 *     <li>Autoriser le frontend Angular à accéder à l’API REST</li>
 *     <li>Gérer les en-têtes HTTP nécessaires aux requêtes CORS</li>
 *     <li>Permettre l’envoi des cookies (ex : JWT stocké côté client)</li>
 * </ul>
 *
 * <p><strong>Exemple :</strong> Si ton frontend tourne sur <code><a href="http://localhost:4200">...</a></code>,
 * cette configuration permet à Angular d’appeler l’API sans erreur CORS.</p>
 */
@Configuration
public class WebConfig {

    /**
     * Configuration CORS pour autoriser les échanges entre le frontend et le backend.
     *
     * @return une instance de {@link WebMvcConfigurer} avec les règles CORS appliquées.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200") // Angular
                        .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                        .allowCredentials(true);
            }
        };
    }
}
