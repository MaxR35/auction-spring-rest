package fr.rougeux.projet.auction.controller;

import fr.rougeux.projet.auction.dto.bo.CategoryDto;
import fr.rougeux.projet.auction.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des catégories.
 * <p>
 * Expose les endpoints permettant de récupérer les catégories via l'API.
 * Les réponses sont fournies sous forme de {@link CategoryDto}.
 * </p>
 * <p>
 * Toutes les requêtes nécessitent que l'utilisateur ait le rôle <code>USER</code>.
 * </p>
 */
@RestController
@RequestMapping("api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Constructeur du contrôleur.
     *
     * @param categoryService le service de gestion des catégories injecté par Spring
     */
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Récupère la liste de toutes les catégories.
     * <p>
     * Accessible uniquement aux utilisateurs avec le rôle <code>USER</code>.
     * Cette méthode délègue la récupération des catégories au {@link CategoryService}.
     * </p>
     *
     * @return une liste de {@link CategoryDto} représentant toutes les catégories
     */
    @PreAuthorize("hasRole('USER')")
    @RequestMapping("")
    public List<CategoryDto> getCategories() {
        return categoryService.findAll();
    }
}
