package fr.rougeux.projet.auction.service;

import fr.rougeux.projet.auction.dto.bo.UserDto;

/**
 * Service pour la gestion des utilisateurs.
 * <p>
 * Définit le contrat pour la récupération d'informations utilisateur
 * à partir de leur adresse email. Les implémentations doivent gérer
 * la conversion des entités {@link fr.rougeux.projet.auction.bo.User} en {@link UserDto}.
 * </p>
 *
 * @author Rougeux Max
 * @version 1.0
 */
public interface UserService {

    /**
     * Récupère un utilisateur à partir de son adresse email.
     * <p>
     * L'implémentation doit lever {@link fr.rougeux.projet.auction.exception.NotFoundException}
     * si aucun utilisateur n'est trouvé pour l'email donné.
     * </p>
     *
     * @param email l'adresse email de l'utilisateur
     * @return le {@link UserDto} correspondant à l'utilisateur
     */
    UserDto findByEmail(String email);
}
