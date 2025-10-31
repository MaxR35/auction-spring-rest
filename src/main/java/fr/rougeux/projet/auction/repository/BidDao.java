package fr.rougeux.projet.auction.repository;

import fr.rougeux.projet.auction.bo.Bid;

import java.util.List;

/**
 * Interface DAO (Data Access Object) pour la gestion des enchères ({@link Bid}).
 *
 * <p>Cette interface définit les opérations principales permettant de manipuler
 * les données des enchères dans la base, notamment la lecture et la création.</p>
 *
 * <p>L’implémentation standard est fournie par {@code BidDaoImpl},
 * utilisant {@link org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate}.</p>
 *
 * <p>Les entités {@link Bid} sont typiquement associées à une {@code Sale}
 * et à un {@code User}.</p>
 *
 * @author Rougeux Max
 * @version 1.0
 */
public interface BidDao {

    /**
     * Récupère toutes les enchères associées à une vente donnée.
     *
     * <p>Les enchères sont généralement triées par montant décroissant
     * afin d’obtenir la plus haute enchère en premier.</p>
     *
     * @param saleId identifiant unique de la vente
     * @return liste des enchères associées à la vente, ou une liste vide si aucune n’existe
     */
    List<Bid> readAll(long saleId);

    /**
     * Crée une nouvelle enchère dans la base de données.
     *
     * <p>L’objet {@link Bid} fourni doit contenir au minimum :
     * <ul>
     *   <li>le montant de l’enchère ({@code bidAmount}),</li>
     *   <li>la date/heure ({@code bidTime}),</li>
     *   <li>l’utilisateur associé ({@code user}),</li>
     *   <li>la vente associée ({@code sale}).</li>
     * </ul>
     * </p>
     *
     * @param bid enchère à insérer dans la base
     */
    void create(Bid bid);
}
