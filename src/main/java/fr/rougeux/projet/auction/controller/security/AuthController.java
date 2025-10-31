package fr.rougeux.projet.auction.controller.security;

import fr.rougeux.projet.auction.configuration.security.jwt.JwtUtils;
import fr.rougeux.projet.auction.dto.bo.UserDto;
import fr.rougeux.projet.auction.dto.request.LoginRequestDto;
import fr.rougeux.projet.auction.dto.response.LoginResponseDto;
import fr.rougeux.projet.auction.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST responsable de la gestion de l'authentification et de la sécurité utilisateur.
 * <p>
 * Ce contrôleur gère :
 * <ul>
 *     <li>La connexion utilisateur avec génération d'un token JWT</li>
 *     <li>La vérification de l'utilisateur connecté</li>
 *     <li>La déconnexion via suppression du cookie JWT</li>
 *     <li>Le rafraîchissement du token</li>
 * </ul>
 * </p>
 *
 * @author Rougeux Max
 * @version 1.0
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    /**
     * Constructeur du contrôleur d'authentification.
     * <p>
     * Initialise les dépendances nécessaires pour la gestion de l'authentification.
     * </p>
     *
     * @param userService le service de gestion des utilisateurs
     * @param passwordEncoder l'encodeur de mots de passe pour la vérification des credentials
     * @param userDetailsService le service Spring Security pour charger les détails utilisateur
     * @param jwtUtils l'utilitaire de gestion des tokens JWT
     */
    public AuthController(UserService userService,
                          PasswordEncoder passwordEncoder,
                          UserDetailsService userDetailsService,
                          JwtUtils jwtUtils) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Retourne les informations de l'utilisateur actuellement connecté.
     * <p>
     * Récupère l'authentification depuis le contexte de sécurité Spring
     * et retourne les données de l'utilisateur associé à l'email authentifié.
     * </p>
     *
     * @return un {@link UserDto} contenant les informations de l'utilisateur connecté
     */
    @GetMapping("/me")
    public UserDto authVerif() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userService.findByEmail(email);
    }

    /**
     * Authentifie un utilisateur et génère un token JWT.
     * <p>
     * Cette méthode :
     * <ul>
     *     <li>Vérifie les identifiants de l'utilisateur</li>
     *     <li>Génère un token JWT en cas de succès</li>
     *     <li>Stocke le token dans un cookie HTTP-only</li>
     *     <li>Retourne les informations de connexion incluant le token et les rôles</li>
     * </ul>
     * </p>
     *
     * @param loginRequestDto l'objet contenant les identifiants de connexion (username et password)
     * @param response la réponse HTTP pour ajouter le cookie JWT
     * @return un {@link LoginResponseDto} contenant le token, le nom d'utilisateur et les rôles
     * @throws BadCredentialsException si les identifiants sont invalides
     */
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto loginRequestDto,
                                  HttpServletResponse response) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.getUsername());

        if(!passwordEncoder.matches(loginRequestDto.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        String token = jwtUtils.generateToken(userDetails);

        // TODO: Voir si les roles sont pertianants pour le client
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        response.addCookie(setCookie(3600, token));

        return new LoginResponseDto(token, userDetails.getUsername(), roles);
    }

    /**
     * Déconnecte l'utilisateur actuellement connecté.
     * <p>
     * Cette méthode :
     * <ul>
     *     <li>Supprime le cookie JWT en définissant son maxAge à 0</li>
     *     <li>Vide le contexte de sécurité Spring</li>
     *     <li>Retourne un message de confirmation</li>
     * </ul>
     * </p>
     *
     * @param response la réponse HTTP pour supprimer le cookie JWT
     * @return une {@link ResponseEntity} contenant un message de succès
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletResponse response) {
        response.addCookie(setCookie(0, null));
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok("Logged out successfully");
    }

    /**
     * Crée et configure un cookie JWT avec les paramètres de sécurité appropriés.
     * <p>
     * Le cookie est configuré avec :
     * <ul>
     *     <li>HttpOnly activé pour prévenir l'accès JavaScript</li>
     *     <li>Secure désactivé en développement (à activer en production avec HTTPS)</li>
     *     <li>Path défini sur "/" pour une disponibilité globale</li>
     *     <li>MaxAge personnalisable (0 pour suppression, 3600 pour 1 heure)</li>
     * </ul>
     * </p>
     *
     * @param maxAge la durée de vie du cookie en secondes (0 pour supprimer le cookie)
     * @param token le token JWT à stocker dans le cookie (peut être null lors de la suppression)
     * @return un {@link Cookie} configuré avec les paramètres de sécurité
     */
    private Cookie setCookie(int maxAge, String token) {

        Cookie cookie = new Cookie("JWT", token);
        cookie.setHttpOnly(true);
        // TODO: Passer à true en production avec HTTPS
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);

        return cookie;
    }
}
