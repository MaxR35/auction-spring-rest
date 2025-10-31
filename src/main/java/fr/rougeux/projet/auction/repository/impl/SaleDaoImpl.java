package fr.rougeux.projet.auction.repository.impl;

import fr.rougeux.projet.auction.bo.Category;
import fr.rougeux.projet.auction.bo.Item;
import fr.rougeux.projet.auction.bo.Sale;
import fr.rougeux.projet.auction.bo.User;
import fr.rougeux.projet.auction.repository.SaleDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Implémentation du DAO pour l'entité {@link Sale}.
 * <p>
 * Cette classe permet de récupérer les ventes depuis la base de données
 * en utilisant Spring JDBC avec des paramètres nommés.
 * Elle mappe également les relations {@link User} (seller),
 * {@link Item} et {@link Category} si nécessaire.
 * </p>
 */
@Repository
public class SaleDaoImpl implements SaleDao {

    /**
     * Template JDBC avec support des paramètres nommés.
     * <p>
     * Utilisé pour exécuter les requêtes SQL avec des paramètres nommés,
     * offrant une meilleure lisibilité et sécurité que les paramètres positionnels.
     * </p>
     */
    public final NamedParameterJdbcTemplate jdbc;

    /**
     * Constructeur du DAO vente.
     * <p>
     * Initialise le DAO avec le template JDBC nécessaire pour l'exécution des requêtes.
     * </p>
     *
     * @param jdbc le template JDBC avec paramètres nommés injecté par Spring
     */
    public SaleDaoImpl(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Récupère toutes les ventes de la base de données.
     * <p>
     * Effectue un LEFT OUTER JOIN sur les tables USERS, ITEMS et CATEGORIES
     * pour mapper les relations {@link User}, {@link Item} et {@link Category}.
     * </p>
     *
     * @return une liste de {@link Sale} avec relations chargées
     */
    @Override
    public List<Sale> readAll() {
        String query = """
                SELECT s.sale_id, s.starting_date, s.ending_date, s.starting_price,
                       MAX(b.bid_amount) AS sale_price,
                       u.user_id, u.last_name, u.first_name, u.user_img,
                       i.item_id, i.item_name, i.item_img, i.item_desc,
                       c.category_id, c.label
                FROM SALES s
                LEFT OUTER JOIN BIDS b ON s.sale_id = b.sale_id
                LEFT OUTER JOIN USERS u ON s.seller_id = u.user_id
                LEFT OUTER JOIN ITEMS i ON s.item_id = i.item_id
                LEFT OUTER JOIN CATEGORIES c ON i.category_id = c.category_id
                GROUP BY s.sale_id, s.starting_date, s.ending_date, s.starting_price, u.user_id, u.last_name, u.first_name, u.user_img, i.item_id, i.item_name, i.item_img, i.item_desc, c.category_id, c.label
                """;

        return jdbc.query(query, new SaleRowMapper(true));
    }

    /**
     * Récupère une vente par son identifiant.
     * <p>
     * Effectue un LEFT OUTER JOIN sur les tables USERS, ITEMS et CATEGORIES
     * pour mapper les relations {@link User}, {@link Item} et {@link Category}.
     * </p>
     *
     * @param saleId l'identifiant de la vente à récupérer
     * @return l'objet {@link Sale} correspondant, avec relations chargées
     */
    @Override
    public Sale readById(long saleId) {
        String query = """
                SELECT s.sale_id, s.starting_date, s.ending_date, s.starting_price, s.sale_price,
                       u.user_id, u.last_name, u.first_name, u.user_img,
                       i.item_id, i.item_name, i.item_img, i.item_desc,
                       c.category_id, c.label
                 FROM SALES s
                 LEFT OUTER JOIN USERS u ON s.seller_id = u.user_id
                 LEFT OUTER JOIN ITEMS i ON s.item_id = i.item_id
                 LEFT OUTER JOIN CATEGORIES c ON i.category_id = c.category_id
                 WHERE s.sale_id = :id
                """;

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", saleId);

        return jdbc.queryForObject(query, paramSource, new SaleRowMapper(true));
    }

    /**
     * Récupère toutes les ventes réalisées par un utilisateur spécifique.
     * <p>
     * Effectue un LEFT OUTER JOIN sur ITEMS et CATEGORIES pour mapper les relations.
     * Le seller n'est pas mappé, car la méthode est filtrée par l'identifiant du seller.
     * </p>
     *
     * @param userId l'identifiant de l'utilisateur
     * @return une liste de {@link Sale} vendues par l'utilisateur
     */
    @Override
    public List<Sale> readByUserId(long userId) {
        String query = """
                SELECT s.sale_id, s.starting_date, s.ending_date, s.starting_price, s.seller_id,
                       i.item_id, i.item_name, i.item_img, i.item_desc,
                       c.category_id, c.label
                FROM SALES s
                LEFT OUTER JOIN ITEMS i ON s.item_id = i.item_id
                LEFT OUTER JOIN CATEGORIES c ON i.category_id = c.category_id
                WHERE s.seller_id = :id
                """;

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", userId);

        return jdbc.query(query, paramSource, new SaleRowMapper(false));
    }

    // =========================
    // ROW MAPPERS
    // =========================

    /**
     * RowMapper pour l'entité {@link Sale}.
     * <p>
     * Permet de mapper une ligne de résultat SQL en {@link Sale} avec ses relations :
     * {@link User} (optionnel), {@link Item} et {@link Category}.
     * </p>
     *
     * @param withSeller indique si la relation {@link User} doit être mappée
     */
    private record SaleRowMapper(boolean withSeller) implements RowMapper<Sale> {
        @Override
        public Sale mapRow(ResultSet rs, int rowNum) throws SQLException {
            Sale sale = new Sale();

            sale.setSaleId(rs.getLong("sale_id"));
            sale.setStartingDate(rs.getTimestamp("starting_date").toLocalDateTime());
            sale.setEndingDate(rs.getTimestamp("ending_date").toLocalDateTime());
            sale.setStartingPrice(rs.getInt("starting_price"));
            sale.setSalePrice(rs.getInt("sale_price"));

            // Seller Mapping
            if(withSeller) {
                User seller = new User();
                seller.setUserId(rs.getLong("user_id"));
                seller.setLastName(rs.getString("last_name"));
                seller.setFirstName(rs.getString("first_name"));
                seller.setUserImg(rs.getString("user_img"));

                sale.setSeller(seller);
            }

            // Category Mapping
            Category category = new Category();
            category.setCategoryId(rs.getLong("category_id"));
            category.setLabel(rs.getString("label"));

            // Item Mapping
            Item item = new Item();
            item.setItemId(rs.getLong("item_id"));
            item.setItemName(rs.getString("item_name"));
            item.setItemImg(rs.getString("item_img"));
            item.setItemDesc(rs.getString("item_desc"));

            item.setCategory(category);
            sale.setItem(item);

            return sale;
        }
    }
}
