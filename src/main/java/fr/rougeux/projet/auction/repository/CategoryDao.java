package fr.rougeux.projet.auction.repository;

import fr.rougeux.projet.auction.bo.Category;

import java.util.List;

/**
 * DAO pour l'accès aux données des catégories.
 * <p>
 * Définit le contrat pour récupérer les entités {@link Category} depuis la base de données.
 * Les implémentations doivent gérer l'accès à la table CATEGORIES et le mapping des résultats SQL
 * vers l'objet {@link Category}.
 * </p>
 *
 * @author Rougeux Max
 * @version 1.0
 */
public interface CategoryDao {

    /**
     * Récupère toutes les catégories présentes dans la base de données.
     * <p>
     * Les informations récupérées peuvent inclure le nombre d'items associés à chaque catégorie.
     * L'implémentation doit retourner une liste vide si aucune catégorie n'est trouvée.
     * </p>
     *
     * @return une liste de {@link Category} représentant toutes les catégories
     */
    List<Category> readAll();
}
