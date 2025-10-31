package fr.rougeux.projet.auction.repository.impl;

import fr.rougeux.projet.auction.bo.Bid;
import fr.rougeux.projet.auction.bo.User;
import fr.rougeux.projet.auction.repository.BidDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Implémentation JDBC du DAO {@link BidDao} pour la gestion des enchères.
 *
 * <p>Cette classe interagit directement avec la base de données via
 * {@link NamedParameterJdbcTemplate} pour exécuter des requêtes SQL
 * paramétrées.</p>
 *
 * <p>Elle gère :
 * <ul>
 *   <li>la lecture de toutes les enchères liées à une vente,</li>
 *   <li>la création d'une nouvelle enchère.</li>
 * </ul>
 * </p>
 *
 * <p>Le mappage des résultats SQL vers les objets {@link Bid} et {@link User}
 * est réalisé par la classe interne {@link BidRowMapper}.</p>
 *
 * @author Rougeux Max
 * @version 1.0
 */
@Repository
public class BidDaoImpl implements BidDao {

    private final NamedParameterJdbcTemplate jdbc;

    /**
     * Constructeur d’injection du {@link NamedParameterJdbcTemplate}.
     *
     * @param jdbc instance de template JDBC utilisée pour exécuter les requêtes
     */
    public BidDaoImpl(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Récupère la liste de toutes les enchères associées à une vente donnée.
     *
     * <p>Les enchères sont triées par montant décroissant afin que
     * la plus haute enchère apparaisse en premier.</p>
     *
     * @param saleId identifiant unique de la vente
     * @return liste des enchères liées à la vente, triées par montant
     */
    @Override
    public List<Bid> readAll(long saleId) {
        String query = """
                    SELECT b.bid_id, b.bid_amount, b.bid_time,
                           u.user_id, u.last_name, u.first_name, u.user_img
                    FROM BIDS b
                    LEFT OUTER JOIN USERS u ON u.user_id = b.user_id
                    WHERE b.sale_id = :id
                    ORDER BY b.bid_amount DESC
                """;

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("id", saleId);

        return jdbc.query(query, paramSource, new BidRowMapper());
    }

    /**
     * Crée une nouvelle enchère dans la base de données.
     *
     * <p>Les informations de l'enchère sont extraites de l’objet {@link Bid}
     * fourni, y compris les identifiants de l’utilisateur et de la vente.</p>
     *
     * @param bid l’objet représentant l’enchère à insérer
     */
    @Override
    public void create(Bid bid) {
        String query = """
                    INSERT INTO BIDS (bid_amount, bid_time, user_id, sale_id)
                    VALUES (:amount, :time, :userId, :saleId)
                """;

        MapSqlParameterSource paramSource = new MapSqlParameterSource();
        paramSource.addValue("amount", bid.getBidAmount());
        paramSource.addValue("time", bid.getBidTime());
        paramSource.addValue("userId", bid.getUser().getUserId());
        paramSource.addValue("saleId", bid.getSale().getSaleId());

        jdbc.update(query, paramSource);
    }

    // =========================================
    // ROW MAPPER
    // =========================================

    /**
     * Mapper SQL-Java pour convertir une ligne de la table <b>BIDS</b>
     * en un objet métier {@link Bid}, incluant les informations de l’utilisateur associé.
     */
    private static class BidRowMapper implements RowMapper<Bid> {
        @Override
        public Bid mapRow(ResultSet rs, int rowNum) throws SQLException {
            // Bid mapping
            Bid bid = new Bid();

            bid.setBidId(rs.getLong("bid_id"));
            bid.setBidAmount(rs.getInt("bid_amount"));
            bid.setBidTime(rs.getObject("bid_time", LocalDateTime.class));

            // User mapping
            User user = new User();

            user.setUserId(rs.getLong("user_id"));
            user.setLastName(rs.getString("last_name"));
            user.setFirstName(rs.getString("first_name"));
            user.setUserImg(rs.getString("user_img"));

            bid.setUser(user);

            return bid;
        }
    }
}
