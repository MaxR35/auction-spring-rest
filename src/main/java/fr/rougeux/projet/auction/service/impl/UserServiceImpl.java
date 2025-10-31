package fr.rougeux.projet.auction.service.impl;

import fr.rougeux.projet.auction.dto.bo.UserDto;
import fr.rougeux.projet.auction.exception.NotFoundException;
import fr.rougeux.projet.auction.repository.UserDao;
import fr.rougeux.projet.auction.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 * Implémentation du service de gestion des utilisateurs.
 * <p>
 * Cette classe fournit les opérations métier liées aux utilisateurs,
 * notamment la recherche et la gestion des données utilisateur.
 * Elle agit comme une couche intermédiaire entre les contrôleurs et la couche d'accès aux données.
 * </p>
 *
 * @author Rougeux Max
 * @version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class.getName());
    private final UserDao userDao;

    /**
     * Constructeur du service utilisateur.
     * <p>
     * Initialise le service avec le DAO nécessaire pour l'accès aux données utilisateur.
     * </p>
     *
     * @param userDao le DAO pour accéder aux données utilisateur en base de données
     */
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Recherche un utilisateur par son adresse email.
     * <p>
     * Cette méthode récupère les informations d'un utilisateur depuis la base de données
     * en utilisant son adresse email comme critère de recherche. Les données de l'entité
     * sont converties en DTO avant d'être retournées.
     * </p>
     *
     * @param email l'adresse email de l'utilisateur à rechercher (ne doit pas être null)
     * @return un {@link UserDto} contenant les informations de l'utilisateur trouvé
     * @throws NotFoundException si aucun utilisateur ne correspond à l'email fourni
     */
    @Override
    public UserDto findByEmail(String email) {
        try {
            UserDto user = userDao.readByEmail(email).toDTO();
            LOG.info("User found: {}", user);

            return user;
        } catch (EmptyResultDataAccessException e) {
            LOG.error("db.user.notfound", e);
            throw new NotFoundException("User not found.");
        }
    }
}
