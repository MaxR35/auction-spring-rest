package fr.rougeux.projet.auction.repository.impl;

import fr.rougeux.projet.auction.bo.Category;
import fr.rougeux.projet.auction.repository.CategoryDao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implémentation du {@link CategoryDao} pour accéder aux données des catégories.
 * <p>
 * Utilise un {@link NamedParameterJdbcTemplate} pour exécuter les requêtes SQL avec paramètres nommés
 * et mappe automatiquement les résultats vers des objets {@link Category}.
 * </p>
 *
 * @author Rougeux Max
 * @version 1.0
 */
@Repository
public class CategoryDaoImpl implements CategoryDao {

    public final NamedParameterJdbcTemplate jdbc;

    /**
     * Constructeur de l'implémentation du DAO.
     *
     * @param jdbc le template JDBC injecté par Spring pour l'accès à la base de données
     */
    public CategoryDaoImpl(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Récupère toutes les catégories de la base de données, avec le nombre d'items associés.
     * <p>
     * La requête effectue une jointure gauche avec la table ITEMS pour compter le nombre d'items
     * par catégorie. Le mapping des colonnes vers les champs de {@link Category} est automatique
     * grâce à {@link BeanPropertyRowMapper}.
     * </p>
     *
     * @return une liste de {@link Category} représentant toutes les catégories et leur nombre d'items
     */
    @Override
    public List<Category> readAll() {
        String query = """
                SELECT c.category_id, c.label, COUNT(i.category_id) as item_count FROM CATEGORIES c LEFT JOIN ITEMS i ON c.category_id = i.category_id
                group by c.category_id, c.label
                """;

        return jdbc.query(query, new BeanPropertyRowMapper<>(Category.class));
    }
}
