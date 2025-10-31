package fr.rougeux.projet.auction.bo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSale {

    /**
     * Vérifie que la méthode getCurrentPrice retourne bien le prix de départ
     * lorsqu'il n'y a pas de prix de vente ni d'enchères.
     */
    @Test
    void testCurrentPrice_NoSalePriceNoBids() {
        Sale sale = new Sale();
        sale.setStartingPrice(50);

        assertEquals(50, sale.getCurrentPrice());
    }
    /**
     * Vérifie que la méthode getCurrentPrice retourne bien le prix de vente
     * lorsqu'il n'y a pas d'enchères.
     */
    @Test
    void testCurrentPrice_SalePriceNoBids() {
        Sale sale = new Sale();
        sale.setStartingPrice(50);
        sale.setSalePrice(100);

        assertEquals(100, sale.getCurrentPrice());
    }
    /**
     * Vérifie que la méthode getCurrentPrice retourne bien le montant de l'enchère
     * la plus haute enchère lorsque la liste des enchères est définie.
     */
    @Test
    void testCurrentPrice_Bids() {
        Bid bid1 = new Bid();
        bid1.setBidAmount(150);
        Bid bid2 = new Bid();
        bid2.setBidAmount(200);
        Bid bid3 = new Bid();
        bid3.setBidAmount(180);

        Sale sale = new Sale();
        sale.setStartingPrice(50);
        sale.setSalePrice(100);
        sale.setBids(List.of(bid1, bid2, bid3));

        assertEquals(200, sale.getCurrentPrice());
    }
}
