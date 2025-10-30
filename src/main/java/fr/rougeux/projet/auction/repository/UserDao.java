package fr.rougeux.projet.auction.repository;

import fr.rougeux.projet.auction.bo.User;


/**
 * DAO pour l'accès aux données des utilisateurs.
 * <p>
 * Définit le contrat pour récupérer des entités {@link User} depuis la base de données.
 * Les implémentations doivent gérer l'accès aux tables utilisateurs et le mapping
 * des résultats SQL vers l'entité {@link User}.
 * </p>
 */
public interface UserDao {

    /**
     * Récupère un utilisateur à partir de son adresse email.
     * <p>
     * L'implémentation peut lever une exception (par exemple {@link org.springframework.dao.EmptyResultDataAccessException})
     * si aucun utilisateur n'est trouvé pour l'email donné.
     * </p>
     *
     * @param email l'adresse email de l'utilisateur
     * @return l'entité {@link User} correspondant à l'email fourni
     */
    User readUserByEmail(String email);
}
