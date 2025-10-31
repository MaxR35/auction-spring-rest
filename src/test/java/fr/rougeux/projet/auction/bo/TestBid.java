package fr.rougeux.projet.auction.bo;

import fr.rougeux.projet.auction.exception.BusinessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class TestBid {
    /**
     * Vérifie que checkNotNull() lève une exception si l'utilisateur est null.
     */
    @Test
    void testCheckNotNull_UserNull() {
        Bid bid = new Bid();
        bid.setSale(new Sale());
        bid.setUser(null);

        assertThrows(BusinessException.class, bid::checkNotNull);
    }
    /**
     * Vérifie que checkNotNull() lève une exception si la vente est null.
     */
    @Test
    void testCheckNotNull_SaleNull() {
        Bid bid = new Bid();
        bid.setSale(null);
        bid.setUser(new User());

        assertThrows(BusinessException.class, bid::checkNotNull);
    }
    /**
     * Vérifie que checkNotNull() ne lève aucune exception si sale et user sont définis.
     */
    @Test
    void testCheckNotNull_Valid() {
        Bid bid = new Bid();
        bid.setSale(new Sale());
        bid.setUser(new User());

        bid.checkNotNull();
    }
    /**
     * Vérifie que checkHigherBid() lève une exception si le montant de la bid
     * est inférieur au prix actuel de la vente.
     */
    @Test
    void testCheckHigherBid_TooLow() {
        Sale sale = new Sale();
        sale.setStartingPrice(100);

        Bid bid = new Bid();
        bid.setSale(sale);
        bid.setBidAmount(50);

        assertThrows(BusinessException.class, bid::checkHigherBid);
    }
    /**
     * Vérifie que checkHigherBid() passe si la bid est supérieure au prix actuel.
     */
    @Test
    void testCheckHigherBid_Valid() {
        Sale sale = new Sale();
        sale.setStartingPrice(100);

        Bid bid = new Bid();
        bid.setSale(sale);
        bid.setBidAmount(150);

        bid.checkHigherBid();
    }
    /**
     * Vérifie que checkUserNotSeller() lève une exception si l'utilisateur est le vendeur de la vente.
     */
    @Test
    void testCheckUserNotSeller_SameUser() {
        User user = new User();

        Sale sale = new Sale();
        sale.setSeller(user);

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setSale(sale);

        assertThrows(BusinessException.class, bid::checkUserNotSeller);
    }
    /**
     * Vérifie que checkUserNotSeller() ne lève pas d'exception si l'utilisateur n'est pas le vendeur.
     */
    @Test
    void testCheckUserNotSeller_Valid() {
        User userBidder = new User();
        userBidder.setUserId(1L);
        User userSeller = new User();
        userSeller.setUserId(2L);

        Sale sale = new Sale();
        sale.setSeller(userSeller);

        Bid bid = new Bid();
        bid.setUser(userBidder);
        bid.setSale(sale);

        bid.checkUserNotSeller();
    }
    /**
     * Vérifie que checkSaleStatus() lève une exception si la vente est terminée.
     */
    @Test
    void testCheckSaleStatus_Over() {
        Sale sale = new Sale();
        sale.setEndingDate(LocalDateTime.now().minusDays(1));

        Bid bid = new Bid();
        bid.setSale(sale);

        assertThrows(BusinessException.class, bid::checkSaleStatus);
    }
    /**
     * Vérifie que checkSaleStatus() ne lève pas d'exception si la vente est encore en cours.
     */
    @Test
    void testCheckSaleStatus_Valid() {
        Sale sale = new Sale();
        sale.setEndingDate(LocalDateTime.now().plusDays(1));

        Bid bid = new Bid();
        bid.setSale(sale);

        bid.checkSaleStatus();
    }
    /**
     * Vérifie que checkUserCanBid() lève une exception si l'utilisateur n'a pas assez de crédit
     * et qu'il n'a pas d'enchère précédente sur la vente.
     */
    @Test
    void testUserCanBid_CreditTooLow_NoOtherBid() {
        User user = new User();
        user.setCredit(100);

        Sale sale = new Sale();
        sale.setStartingPrice(150);

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setSale(sale);
        bid.setBidAmount(150);

        assertThrows(BusinessException.class, bid::checkUserCanBid);
    }
    /**
     * Vérifie que checkUserCanBid() passe si l'utilisateur a assez de crédit
     * et qu'il n'a pas d'enchère précédente sur la vente.
     */
    @Test
    void testUserCanBid_CreditValid_NoOtherBid() {
        User user = new User();
        user.setCredit(200);

        Sale sale = new Sale();
        sale.setStartingPrice(150);

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setSale(sale);
        bid.setBidAmount(150);

        bid.checkUserCanBid();
    }
    /**
     * Vérifie que checkUserCanBid() lève une exception si l'utilisateur n'a pas assez de crédit
     * pour couvrir la différence avec sa précédente enchère sur la même vente.
     */
    @Test
    void testUserCanBid_CreditTooLow_WithOtherBid() {
        User user = new User();
        user.setUserId(1L);
        user.setCredit(10);

        User user2 = new User();
        user2.setUserId(2L);

        Bid otherBid = new Bid();
        otherBid.setUser(user2);
        otherBid.setBidAmount(150);

        Bid otherUserBid = new Bid();
        otherUserBid.setUser(user);
        otherBid.setBidAmount(140);

        Sale sale = new Sale();
        sale.setStartingPrice(150);
        sale.setBids(List.of(otherBid, otherUserBid));

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setSale(sale);
        bid.setBidAmount(151);

        assertThrows(BusinessException.class, bid::checkUserCanBid);
    }
    /**
     * Vérifie que checkUserCanBid() passe si l'utilisateur a assez de crédit
     * pour couvrir la différence avec sa précédente enchère sur la même vente.
     */
    @Test
    void testUserCanBid_CreditValid_WithOtherBid() {
        User user = new User();
        user.setUserId(1L);
        user.setCredit(20);

        User user2 = new User();
        user2.setUserId(2L);

        Bid otherBid = new Bid();
        otherBid.setUser(user2);
        otherBid.setBidAmount(150);

        Bid otherUserBid = new Bid();
        otherUserBid.setUser(user);
        otherUserBid.setBidAmount(140);

        Sale sale = new Sale();
        sale.setStartingPrice(150);
        sale.setBids(List.of(otherBid, otherUserBid));

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setSale(sale);
        bid.setBidAmount(160);

        bid.checkUserCanBid();
    }

    /**
     * Vérifie que validateBid() passe si validateBid() ne lève aucune exception.
     */
    @Test
    void testValidateBid_AllValid() {
        User user = new User();
        user.setUserId(1L);
        user.setCredit(100);

        User user2 = new User();
        user2.setUserId(2L);

        Bid otherBid = new Bid();
        otherBid.setUser(user2);
        otherBid.setBidAmount(150);

        Bid otherUserBid = new Bid();
        otherUserBid.setUser(user);
        otherUserBid.setBidAmount(140);

        Sale sale = new Sale();
        sale.setStartingPrice(150);
        sale.setEndingDate(LocalDateTime.now().plusDays(1));
        sale.setSeller(user2);
        sale.setBids(List.of(otherBid, otherUserBid));

        Bid bid = new Bid();
        bid.setUser(user);
        bid.setSale(sale);
        bid.setBidAmount(160);

        bid.validateBid();
    }
}
