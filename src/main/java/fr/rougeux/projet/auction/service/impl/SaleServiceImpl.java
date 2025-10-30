package fr.rougeux.projet.auction.service.impl;

import fr.rougeux.projet.auction.bo.Sale;
import fr.rougeux.projet.auction.dto.bo.SaleDto;
import fr.rougeux.projet.auction.exception.NotFoundException;
import fr.rougeux.projet.auction.repository.SaleDao;
import fr.rougeux.projet.auction.service.SaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation du {@link SaleService}.
 * <p>
 * Contient la logique métier des ventes aux enchères :
 * <ul>
 *     <li>Lecture de toutes les ventes avec enchère actuelle</li>
 *     <li>Lecture d’une vente et de ses enchères</li>
 *     <li>Placement d’une enchère avec gestion du crédit utilisateur</li>
 * </ul>
 */
@Service
public class SaleServiceImpl implements SaleService {

    private static final Logger LOG = LoggerFactory.getLogger(SaleServiceImpl.class);
    private final SaleDao saleDao;

    /**
     * Constructeur du service de vente.
     *
     * @param saleDao le DAO utilisé pour accéder aux ventes en base de données
     */
    public SaleServiceImpl(SaleDao saleDao) {
        this.saleDao = saleDao;
    }

    /**
     * Récupère toutes les ventes de la base de données.
     *
     * @return une liste de {@link SaleDto} représentant toutes les ventes
     * @throws DataAccessException si un problème survient lors de l'accès à la base
     */
    @Override
    public List<SaleDto> findAll() {
        try {
            LOG.info("Fetching all sales from the database.");
            List<Sale> sales = saleDao.readAll();

            return sales.stream().map(Sale::toDTO).toList();
        } catch (DataAccessException e) {
            LOG.error("db.access.error");
            throw e;
        }
    }

    /**
     * Récupère une vente par son identifiant.
     *
     * @param id l'identifiant de la vente
     * @return le {@link SaleDto} correspondant
     * @throws NotFoundException si aucune vente n'est trouvée pour l'identifiant donné
     */
    @Override
    public SaleDto findById(long id) {
        try {
            SaleDto sale = saleDao.readById(id).toDTO();
            LOG.info("Sale found: {}", sale);

            return sale;
        } catch (EmptyResultDataAccessException e) {
            LOG.error("db.sale.notfound", e);
            throw new NotFoundException("Sale not found.");
        }
    }

    /**
     * Récupère toutes les ventes associées à un utilisateur donné.
     * <p>
     * Si l'utilisateur n'a pas de ventes, une liste vide est retournée.
     * L'existence de l'utilisateur est supposée vérifiée en amont (par exemple dans le controller).
     * </p>
     *
     * @param userId l'identifiant de l'utilisateur
     * @return une liste de {@link SaleDto} représentant les ventes de l'utilisateur
     */
    @Override
    public List<SaleDto> findByUserId(long userId) {
        LOG.info("Fetching all user sales from the database.");
        List<Sale> sales = saleDao.readByUserId(userId);

        return sales.stream().map(Sale::toDTO).toList();
    }
}
