package fr.rougeux.projet.auction.controller;

import fr.rougeux.projet.auction.dto.bo.SaleDto;
import fr.rougeux.projet.auction.service.SaleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des ventes (Sales).
 * <p>
 * Cette classe expose les endpoints liés aux ventes aux enchères :
 * <ul>
 *     <li>Récupération de toutes les ventes</li>
 *     <li>Consultation d’une vente spécifique par son identifiant</li>
 *     <li>Enchérir sur une vente existante</li>
 * </ul>
 * Tous les endpoints sont sécurisés et accessibles uniquement aux utilisateurs
 * disposant du rôle <b>USER</b>.
 * </p>
 *
 * <h3>Routes disponibles :</h3>
 * <ul>
 *     <li><b>GET /api/sales</b> — Récupère la liste de toutes les ventes</li>
 *     <li><b>GET /api/sales/{id}</b> — Récupère les détails d’une vente</li>
 *     <li><b>POST /api/sales/bid</b> — Envoie une enchère sur une vente</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/sales")
public class SaleController {

    private final SaleService saleService;

    /**
     * Constructeur injectant le service de gestion des ventes.
     *
     * @param saleService service métier responsable de la logique des ventes.
     */
    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    /**
     * Récupère la liste de toutes les ventes disponibles.
     * <p>
     * Accessible uniquement aux utilisateurs authentifiés avec le rôle <b>USER</b>.
     * </p>
     *
     * @return une liste de {@link SaleDto} représentant les ventes.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("")
    public List<SaleDto> getVentes() {
        return saleService.findAll();
    }

    /**
     * Récupère une vente spécifique à partir de son identifiant.
     * <p>
     * Accessible uniquement aux utilisateurs authentifiés avec le rôle <b>USER</b>.
     * </p>
     *
     * @param id identifiant unique de la vente.
     * @return un objet {@link SaleDto} représentant la vente demandée.
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}")
    public SaleDto getVenteById(@PathVariable long id) {
        return saleService.findById(id);
    }

}
