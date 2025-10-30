package fr.rougeux.projet.auction.configuration.security.jwt;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/**
 * Service utilitaire pour la génération de tokens JWT (JSON Web Token).
 * <p>
 * Cette classe est responsable de la création de tokens signés contenant les informations
 * d’un utilisateur authentifié. Les tokens générés permettent ensuite à l’application
 * de vérifier l’identité et les rôles de l’utilisateur sans nécessiter de session serveur.
 * </p>
 *
 * <h3>Fonctionnalités principales :</h3>
 * <ul>
 *     <li>Génération de tokens avec une durée de validité d’une heure</li>
 *     <li>Ajout du nom d’utilisateur (subject) et des rôles dans les claims</li>
 *     <li>Signature HMAC SHA-256 via le {@link JwtEncoder}</li>
 * </ul>
 *
 * <p><strong>Exemple d’utilisation :</strong></p>
 * <pre>{@code
 * @Autowired
 * private JwtUtils jwtUtils;
 *
 * String token = jwtUtils.generateToken(userDetails);
 * }</pre>
 */
@Service
public class JwtUtils {

    private final JwtEncoder jwtEncoder;

    /**
     * Constructeur injectant le {@link JwtEncoder} utilisé pour signer les tokens.
     *
     * @param jwtEncoder encodeur JWT configuré avec la clé secrète.
     */
    public JwtUtils(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    /**
     * Génère un token JWT pour un utilisateur donné.
     * <p>
     * Le token contient les informations suivantes :
     * <ul>
     *     <li><b>issuer</b> – défini comme "self"</li>
     *     <li><b>issuedAt</b> – date et heure de génération</li>
     *     <li><b>expiresAt</b> – date d’expiration (1 heure après émission)</li>
     *     <li><b>subject</b> – nom d’utilisateur</li>
     *     <li><b>roles</b> – liste des rôles de l’utilisateur</li>
     * </ul>
     *
     * @param userDetails les informations de l’utilisateur pour lequel générer le token.
     * @return une chaîne représentant le token JWT signé.
     */
    public String generateToken(UserDetails userDetails) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .build();
        JwtEncoderParameters jwtEncoderParameters = JwtEncoderParameters.from(JwsHeader.with(MacAlgorithm.HS256).build(), claims);
        return this.jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }
}