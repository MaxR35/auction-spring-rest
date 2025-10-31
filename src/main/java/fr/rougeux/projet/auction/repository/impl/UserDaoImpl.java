package fr.rougeux.projet.auction.repository.impl;

import fr.rougeux.projet.auction.bo.User;
import fr.rougeux.projet.auction.repository.UserDao;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Implémentation du DAO (Data Access Object) pour la gestion des utilisateurs.
 * <p>
 * Cette classe fournit l'accès aux données utilisateur stockées en base de données
 * en utilisant JDBC avec des requêtes SQL natives. Elle utilise {@link NamedParameterJdbcTemplate}
 * pour faciliter l'exécution de requêtes paramétrées et prévenir les injections SQL.
 * </p>
 *
 * @author Rougeux Max
 * @version 1.0
 */
@Repository
public class UserDaoImpl implements UserDao {

    /**
     * Template JDBC avec support des paramètres nommés.
     * <p>
     * Utilisé pour exécuter les requêtes SQL avec des paramètres nommés,
     * offrant une meilleure lisibilité et sécurité que les paramètres positionnels.
     * </p>
     */
    public final NamedParameterJdbcTemplate jdbc;

    /**
     * Constructeur du DAO utilisateur.
     * <p>
     * Initialise le DAO avec le template JDBC nécessaire pour l'exécution des requêtes.
     * </p>
     *
     * @param jdbc le template JDBC avec paramètres nommés injecté par Spring
     */
    public UserDaoImpl(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Récupère un utilisateur depuis la base de données par son adresse email.
     * <p>
     * Cette méthode exécute une requête SQL pour rechercher un utilisateur unique
     * correspondant à l'email fourni. Les données sont automatiquement mappées
     * vers un objet {@link User} grâce au {@link BeanPropertyRowMapper}.
     * </p>
     * <p>
     * Colonnes récupérées :
     * <ul>
     *     <li>user_id : identifiant unique de l'utilisateur</li>
     *     <li>last_name : nom de famille</li>
     *     <li>first_name : prénom</li>
     *     <li>email : adresse email</li>
     *     <li>user_img : image de profil</li>
     *     <li>phone : numéro de téléphone</li>
     *     <li>credit : crédit disponible</li>
     * </ul>
     * </p>
     *
     * @param email l'adresse email de l'utilisateur à rechercher (ne doit pas être null)
     * @return l'objet {@link User} correspondant à l'email fourni
     * @throws org.springframework.dao.EmptyResultDataAccessException si aucun utilisateur
     *         ne correspond à l'email
     * @throws org.springframework.dao.IncorrectResultSizeDataAccessException si plusieurs
     *         utilisateurs correspondent à l'email
     * @throws DataAccessException en cas d'erreur d'accès à la base de données
     */
    @Override
    public User readByEmail(String email) {
        String query = """
                SELECT u.user_id, u.last_name, u.first_name, u.email, u.user_img, u.phone, u.credit
                FROM USERS u
                WHERE u.email = :email
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", email);

        return jdbc.queryForObject(query, params, new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void update(User user) {
        String query = """
                UPDATE USERS
                SET last_name = :lastName, first_name = :firstName, email = :email,
                    user_img = :userImg, phone = :phone, credit = :credit
                WHERE user_id = :userId
                """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("lastName", user.getLastName());
        params.addValue("firstName", user.getFirstName());
        params.addValue("email", user.getEmail());
        params.addValue("userImg", user.getUserImg());
        params.addValue("phone", user.getPhone());
        params.addValue("credit", user.getCredit());
        params.addValue("userId", user.getUserId());

        jdbc.update(query, params);
    }
}
