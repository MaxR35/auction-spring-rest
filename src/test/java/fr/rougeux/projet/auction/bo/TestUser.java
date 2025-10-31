package fr.rougeux.projet.auction.bo;

import fr.rougeux.projet.auction.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TestUser {

    /**
     * Vérifie que canBid lance une BusinessException lorsque les crédits sont insuffisants.
     */
    @Test
    void testCanBid_InsufficientCredit() {
        User user = new User();
        user.setCredit(100);

        int amount = 200;
        assertThrows(BusinessException.class, () -> user.canBid(amount));
    }

    /**
     * Vérifie que canBid déduit correctement le montant des crédits lorsque ceux-ci sont suffisants.
     */
    @Test
    void testCanBid_Valid() {
        User user = new User();
        user.setCredit(500);

        int amount = 200;
        user.canBid(amount);

        assertEquals(300, user.getCredit());
    }
}
