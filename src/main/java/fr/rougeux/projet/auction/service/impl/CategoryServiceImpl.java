package fr.rougeux.projet.auction.service.impl;

import fr.rougeux.projet.auction.bo.Category;
import fr.rougeux.projet.auction.dto.bo.CategoryDto;
import fr.rougeux.projet.auction.repository.CategoryDao;
import fr.rougeux.projet.auction.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation du {@link CategoryService}.
 * <p>
 * Fournit la logique métier pour la gestion des catégories :
 * <ul>
 *     <li>Lecture de toutes les catégories</li>
 *     <li>Conversion des entités {@link Category} en {@link CategoryDto}</li>
 * </ul>
 * Cette classe utilise un {@link CategoryDao} pour accéder aux données dans la base.
 * </p>
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryDao categoryDao;

    /**
     * Constructeur de l'implémentation du service.
     *
     * @param dao le {@link CategoryDao} injecté par Spring pour accéder aux données des catégories
     */
    public CategoryServiceImpl(CategoryDao dao) {
        this.categoryDao = dao;
    }

    /**
     * Récupère toutes les catégories de la base de données et les convertit en {@link CategoryDto}.
     * <p>
     * En cas d'erreur d'accès à la base de données, une exception {@link DataAccessException} est levée.
     * </p>
     *
     * @return une liste de {@link CategoryDto} représentant toutes les catégories
     */
    @Override
    public List<CategoryDto> findAll() {
        try {
            LOG.info("Fetching categories from the database.");
            List<Category> categories = categoryDao.readAll();

            return categories.stream().map(Category::toDTO).toList();
        } catch (DataAccessException e) {
            LOG.error("db.access.error");
            throw e;
        }
    }
}
