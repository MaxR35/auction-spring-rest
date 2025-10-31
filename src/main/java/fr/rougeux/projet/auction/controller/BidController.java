package fr.rougeux.projet.auction.controller;

import fr.rougeux.projet.auction.dto.request.BidRequestDto;
import fr.rougeux.projet.auction.dto.response.BidResponseDto;
import fr.rougeux.projet.auction.exception.BusinessException;
import fr.rougeux.projet.auction.exception.NotFoundException;
import fr.rougeux.projet.auction.service.BidService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Contrôleur REST gérant les opérations liées aux enchères (bids).
 *
 * <p>Ce contrôleur permet à un utilisateur authentifié de placer une enchère
 * sur une vente donnée via l'endpoint <code>/api/bid/place</code>.</p>
 *
 * <p>L'accès à ces opérations est restreint aux utilisateurs ayant le rôle <b>USER</b>.</p>
 *
 * @author Rougeux Max
 * @version 1.0
 */
@RestController
@RequestMapping("api/bid")
public class BidController {

    private final BidService bidService;

    /**
     * Constructeur d’injection du service des enchères.
     *
     * @param bidService service métier gérant la logique des enchères
     */
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    /**
     * Permet à un utilisateur connecté de placer une nouvelle enchère sur une vente.
     *
     * <p>Cette méthode vérifie les règles métier (crédit utilisateur, statut de la vente, etc.)
     * avant d’enregistrer l’enchère. En cas d’erreur (enchère trop basse, vente terminée, etc.),
     * une exception métier est levée et renvoyée sous forme d’erreur HTTP appropriée.</p>
     *
     * @param bidRequest données de l’enchère à placer (identifiant de la vente, montant, etc.)
     * @return un objet {@link BidResponseDto} contenant les informations de la vente mise à jour
     *         et celles de l’utilisateur après placement de l’enchère
     *
     * @throws NotFoundException si la vente ou l’utilisateur n’existe pas
     * @throws BusinessException si la règle métier d’enchère n’est pas respectée
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/place")
    public BidResponseDto placeBid(@RequestBody BidRequestDto bidRequest) {
        return bidService.placeBid(bidRequest);
    }
}
