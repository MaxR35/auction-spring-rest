package fr.rougeux.projet.auction.service;

import fr.rougeux.projet.auction.dto.bo.CategoryDto;

import java.util.List;

/**
 * Service pour la gestion des catégories.
 * <p>
 * Définit le contrat pour la récupération des catégories et la conversion des entités
 * en {@link CategoryDto} pour l’envoi vers la couche de présentation.
 * </p>
 */
public interface CategoryService {

    /**
     * Récupère toutes les catégories disponibles.
     * <p>
     * Les implémentations doivent gérer la logique métier associée et retourner
     * les catégories sous forme de {@link CategoryDto}. En cas d'absence de catégories,
     * une liste vide doit être retournée.
     * </p>
     *
     * @return une liste de {@link CategoryDto} représentant toutes les catégories
     */
    List<CategoryDto> findAll();
}
