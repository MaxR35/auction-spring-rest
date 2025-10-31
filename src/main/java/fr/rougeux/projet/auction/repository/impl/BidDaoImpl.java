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

@Repository
public class BidDaoImpl implements BidDao {

    private final NamedParameterJdbcTemplate jdbc;

    public BidDaoImpl(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

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

    // =========================================
    // ROW MAPPER
    // =========================================

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
