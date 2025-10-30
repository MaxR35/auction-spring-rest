package fr.rougeux.projet.auction.service;

import fr.rougeux.projet.auction.dto.bo.SaleDto;

import java.util.List;

/**
 * Service pour la gestion des ventes aux enchères.
 * <p>
 * Définit le contrat pour la récupération des ventes et l'accès aux informations
 * relatives à une vente ou à un utilisateur. Les implémentations doivent fournir
 * la conversion des entités {@link fr.rougeux.projet.auction.bo.Sale} en {@link SaleDto}.
 * </p>
 */
public interface SaleService {

    /**
     * Récupère toutes les ventes de la base de données.
     * <p>
     * L'implémentation peut lever une exception en cas de problème d'accès à la base.
     * </p>
     *
     * @return une liste de {@link SaleDto} représentant toutes les ventes
     */
    List<SaleDto> findAll();

    /**
     * Récupère toutes les ventes associées à un utilisateur donné.
     * <p>
     * Si l'utilisateur n'a pas de ventes, une liste vide est retournée.
     * L'existence de l'utilisateur peut être vérifiée en amont, selon le contexte.
     * </p>
     *
     * @param userId l'identifiant de l'utilisateur
     * @return une liste de {@link SaleDto} représentant les ventes de l'utilisateur
     */
    List<SaleDto> findByUserId(long userId);

    /**
     * Récupère une vente par son identifiant.
     * <p>
     * L'implémentation doit lever {@link fr.rougeux.projet.auction.exception.NotFoundException}
     * si aucune vente n'est trouvée pour l'identifiant donné.
     * </p>
     *
     * @param id l'identifiant de la vente
     * @return le {@link SaleDto} correspondant
     */
    SaleDto findById(long id);
}
