package fr.rougeux.projet.auction.service;

import fr.rougeux.projet.auction.dto.request.BidRequestDto;
import fr.rougeux.projet.auction.dto.response.BidResponseDto;
import fr.rougeux.projet.auction.exception.BusinessException;
import fr.rougeux.projet.auction.exception.NotFoundException;

/**
 * Service métier gérant les opérations liées aux enchères ({@link fr.rougeux.projet.auction.bo.Bid}).
 *
 * <p>Cette interface définit le contrat fonctionnel permettant à un utilisateur
 * de placer une enchère sur une vente en cours.
 * Son implémentation concrète ({@link fr.rougeux.projet.auction.service.impl.BidServiceImpl})
 * assure la coordination entre les couches DAO, sécurité et logique métier.</p>
 *
 * <p>Le service garantit notamment que :
 * <ul>
 *     <li>la vente et l’utilisateur existent,</li>
 *     <li>les règles métier de validation d’enchère sont respectées,</li>
 *     <li>les opérations de persistance sont exécutées de manière transactionnelle.</li>
 * </ul>
 * </p>
 *
 * @see fr.rougeux.projet.auction.service.impl.BidServiceImpl
 * @see fr.rougeux.projet.auction.bo.Bid
 * @see fr.rougeux.projet.auction.dto.request.BidRequestDto
 * @see fr.rougeux.projet.auction.dto.response.BidResponseDto
 *
 * @author Rougeux Max
 * @version 1.0
 */
public interface BidService {

    /**
     * Place une nouvelle enchère sur une vente donnée pour l’utilisateur connecté.
     *
     * <p>Cette méthode :
     * <ol>
     *     <li>Valide l’intégrité et la cohérence des données,</li>
     *     <li>Vérifie les règles métier (enchère supérieure, crédit suffisant, etc.),</li>
     *     <li>Persiste l’enchère et met à jour le crédit de l’utilisateur.</li>
     * </ol>
     * </p>
     *
     * @param bidRequest DTO contenant les informations de l’enchère à placer
     * @return {@link BidResponseDto} contenant la vente actualisée et l’utilisateur mis à jour
     * @throws NotFoundException si la vente ou l’utilisateur n’existent pas
     * @throws BusinessException si une règle métier empêche le placement de l’enchère
     */
    BidResponseDto placeBid(BidRequestDto bidRequest);
}
