package fr.rougeux.projet.auction.service.impl;

import fr.rougeux.projet.auction.bo.Bid;
import fr.rougeux.projet.auction.bo.Sale;
import fr.rougeux.projet.auction.bo.User;
import fr.rougeux.projet.auction.dto.request.BidRequestDto;
import fr.rougeux.projet.auction.dto.response.BidResponseDto;
import fr.rougeux.projet.auction.exception.BusinessException;
import fr.rougeux.projet.auction.exception.NotFoundException;
import fr.rougeux.projet.auction.repository.BidDao;
import fr.rougeux.projet.auction.repository.SaleDao;
import fr.rougeux.projet.auction.repository.UserDao;
import fr.rougeux.projet.auction.service.BidService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service {@link BidService} gérant la logique métier des enchères.
 *
 * <p>Cette classe orchestre la création d’une enchère, incluant :
 * <ul>
 *     <li>la vérification de l’existence de la vente et de l’utilisateur,</li>
 *     <li>la validation métier via le BO {@link Bid},</li>
 *     <li>la persistance des données ({@link BidDao}, {@link UserDao}),</li>
 *     <li>et la préparation de la réponse pour l’API.</li>
 * </ul>
 *
 * <p>Toutes les opérations critiques sont exécutées dans une transaction
 * (grâce à {@link Transactional}) pour garantir la cohérence entre
 * la création de l’enchère et la mise à jour du crédit utilisateur.</p>
 *
 * <p>Les exceptions levées sont de deux types :
 * <ul>
 *     <li>{@link NotFoundException} – si la vente ou l’utilisateur n’existent pas,</li>
 *     <li>{@link BusinessException} – si une règle métier échoue (ex : crédit insuffisant, enchère trop basse, etc.).</li>
 * </ul>
 *
 * @see Bid
 * @see Sale
 * @see User
 * @see BidDao
 * @see SaleDao
 * @see UserDao
 *
 * @author Rougeux Max
 * @version 1.0
 */
@Service
public class BidServiceImpl implements BidService {

    private final SaleDao saleDao;
    private final BidDao bidDao;
    private final UserDao userDao;

    /**
     * Constructeur principal du service d’enchères.
     *
     * @param saleDao DAO de gestion des ventes
     * @param bidDao DAO de gestion des enchères
     * @param userDao DAO de gestion des utilisateurs
     */
    public BidServiceImpl(SaleDao saleDao, BidDao bidDao, UserDao userDao) {
        this.saleDao = saleDao;
        this.bidDao = bidDao;
        this.userDao = userDao;
    }

    /**
     * Place une enchère sur une vente existante pour l’utilisateur actuellement connecté.
     *
     * <p>Cette méthode :
     * <ol>
     *     <li>Récupère la vente associée et ses enchères existantes,</li>
     *     <li>Récupère l’utilisateur à partir du contexte de sécurité,</li>
     *     <li>Construit et valide l’objet {@link Bid},</li>
     *     <li>Persiste la nouvelle enchère et met à jour le crédit utilisateur,</li>
     *     <li>Retourne un {@link BidResponseDto} prêt pour l’API.</li>
     * </ol>
     * </p>
     *
     * <p>En cas de données inexistantes, une {@link NotFoundException} est levée.</p>
     * <p>En cas de violation métier (enchère trop faible, utilisateur sans crédit, etc.), une {@link BusinessException} est levée.</p>
     *
     * @param bidRequest DTO contenant les informations de l’enchère à placer
     * @return {@link BidResponseDto} contenant la vente mise à jour et l’utilisateur actualisé
     * @throws NotFoundException si la vente ou l’utilisateur n’existent pas
     * @throws BusinessException si la validation métier échoue
     */
    @Override
    @Transactional
    public BidResponseDto placeBid(BidRequestDto bidRequest) {
        Sale sale;
        try {
            sale = saleDao.readById(bidRequest.getSaleId());
            sale.setBids(bidDao.readAll(sale.getSaleId()));
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("sale.not.found");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user;
        try {
            user = userDao.readByEmail(authentication.getName());
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("user.not.found");
        }

        Bid bid = new Bid();
        bid.setSale(sale);
        bid.setUser(user);
        bid.setBidAmount(bidRequest.getBidAmount());
        bid.setBidTime(bidRequest.getBidTime());

        bid.validateBid();
        bidDao.create(bid);
        userDao.update(bid.getUser());

        Sale updatedSale = new Sale(bid.getSale());
        bid.getSale().setBids(null);
        updatedSale.getBids().addFirst(bid);

        return new BidResponseDto(updatedSale.toDTO(), bid.getUser().toDTO());
    }
}
