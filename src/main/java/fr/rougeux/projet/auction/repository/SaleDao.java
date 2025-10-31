package fr.rougeux.projet.auction.repository;

import fr.rougeux.projet.auction.bo.Sale;

import java.util.List;

/**
 * DAO pour l'accès aux données des ventes.
 * <p>
 * Définit le contrat pour récupérer des entités {@link Sale} depuis la base de données.
 * Les implémentations doivent gérer l'accès aux tables SALES, ITEMS, USERS et CATEGORIES,
 * ainsi que le mapping des résultats SQL vers l'entité {@link Sale}.
 * </p>
 *
 * @author Rougeux Max
 * @version 1.0
 */
public interface SaleDao {

    /**
     * Récupère toutes les ventes présentes dans la base de données.
     * <p>
     * L'implémentation peut effectuer les jointures nécessaires pour inclure
     * les relations {@link fr.rougeux.projet.auction.bo.Item} et {@link fr.rougeux.projet.auction.bo.User}.
     * </p>
     *
     * @return une liste de {@link Sale} représentant toutes les ventes
     */
    List<Sale> readAll();

    /**
     * Récupère une vente à partir de son identifiant.
     * <p>
     * L'implémentation peut lever une exception (par exemple {@link org.springframework.dao.EmptyResultDataAccessException})
     * si aucune vente n'est trouvée pour l'identifiant fourni.
     * </p>
     *
     * @param saleId l'identifiant de la vente
     * @return l'entité {@link Sale} correspondant à l'identifiant
     */
    Sale readById(long saleId);

    /**
     * Récupère toutes les ventes associées à un utilisateur donné.
     * <p>
     * L'implémentation peut effectuer des jointures pour inclure
     * les informations des {@link fr.rougeux.projet.auction.bo.Item} et éventuellement
     * du {@link fr.rougeux.projet.auction.bo.User} si nécessaire.
     * Si l'utilisateur n'a pas de ventes, une liste vide doit être retournée.
     * </p>
     *
     * @param userId l'identifiant de l'utilisateur
     * @return une liste de {@link Sale} représentant les ventes de l'utilisateur
     */
    List<Sale> readByUserId(long userId);
}
