package fr.rougeux.projet.auction.configuration.security;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import javax.sql.DataSource;

/**
 * Configuration principale de la sécurité Spring Security pour l’application.
 * <p>
 * Cette classe configure :
 * <ul>
 *     <li>L’authentification des utilisateurs via la base de données (email / mot de passe)</li>
 *     <li>La gestion des rôles et permissions via JWT</li>
 *     <li>Une politique sans session (API REST stateless)</li>
 *     <li>La récupération du token JWT depuis un cookie HTTP</li>
 * </ul>
 * <p>
 * Le but est d’assurer la protection des endpoints de l’API en fonction du rôle de l’utilisateur
 * tout en maintenant une configuration compatible avec une architecture front-end séparée.
 *
 * @author Rougeux Max
 * @version 1.0
 */
@Configuration
public class SecurityConfig {
    /**
     * Définit le gestionnaire des utilisateurs basé sur JDBC.
     * <p>
     * Il permet à Spring Security de récupérer les utilisateurs et leurs rôles
     * directement depuis la base de données via des requêtes SQL personnalisées.
     *
     * @param dataSource la source de données utilisée pour accéder à la base.
     * @return une instance de {@link UserDetailsManager}.
     */
    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.setUsersByUsernameQuery("""
                SELECT email as username, password, 1 as enabled
                FROM USERS u
                WHERE u.email = ?
                """);

        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("""
                SELECT u.email AS username,
                CASE WHEN u.is_admin = 1 THEN 'ROLE_ADMIN' ELSE 'ROLE_USER' END AS authority
                FROM USERS u
                WHERE u.email = ?
                """);

        return jdbcUserDetailsManager;
    }

    /**
     * Définit le mécanisme de chiffrement des mots de passe.
     *
     * @return un {@link BCryptPasswordEncoder}, recommandé pour stocker les mots de passe de manière sécurisée.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure la chaîne de filtres de sécurité HTTP.
     * <p>
     * Cette configuration désactive la session et CSRF, et définit les règles
     * d’accès aux différentes routes de l’API selon le rôle utilisateur.
     *
     * @param http l’objet {@link HttpSecurity} à configurer.
     * @return une instance de {@link SecurityFilterChain}.
     * @throws Exception en cas d’erreur de configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Désactivation des mécanismes non nécessaires pour une API REST
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Configuration des permissions
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/me").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/auth/logout").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/sales/**").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/api/bid/place").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/api/categories").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/img/**").permitAll()
                        .anyRequest().denyAll()
                )

                // Configuration du serveur de ressources OAuth2 avec JWT
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                        .bearerTokenResolver(cookieBearerTokenResolver())
                )

                // Désactivation des modes de connexion hérités
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }

    /**
     * Définit l’encodeur JWT utilisé pour signer les tokens.
     *
     * @param jwtSecret la clé secrète définie dans les propriétés de l’application.
     * @return une instance de {@link JwtEncoder}.
     */
    @Bean
    public JwtEncoder jwtEncoder(@Value("${app.jwtSecret}") String jwtSecret) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(jwtSecret.getBytes()));
    }

    /**
     * Définit le décodeur JWT utilisé pour valider et décoder les tokens.
     *
     * @param jwtSecret la clé secrète utilisée pour la signature des tokens.
     * @return une instance de {@link JwtDecoder}.
     */
    @Bean
    public JwtDecoder jwtDecoder(@Value("${app.jwtSecret}") String jwtSecret) {
        SecretKeySpec secretKey = new SecretKeySpec(jwtSecret.getBytes(), 0, jwtSecret.getBytes().length, "RSA");
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }

    /**
     * Convertisseur JWT qui extrait les rôles depuis la claim "roles" sans préfixe "ROLE_".
     *
     * @return une instance configurée de {@link JwtAuthenticationConverter}.
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("");
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles");


        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    /**
     * Permet d’extraire le token JWT depuis un cookie nommé "JWT"
     * au lieu de l’en-tête "Authorization".
     * <p>
     * Cela rend la gestion des tokens plus transparente côté front-end (SPA).
     *
     * @return un {@link BearerTokenResolver} personnalisé.
     */
    @Bean
    public BearerTokenResolver cookieBearerTokenResolver() {
        return request -> {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("JWT".equals(cookie.getName())) {
                        return cookie.getValue();
                    }
                }
            }
            return null;
        };
    }
}
