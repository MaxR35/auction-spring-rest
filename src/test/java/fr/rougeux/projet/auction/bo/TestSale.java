package fr.rougeux.projet.auction.bo;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TestSale {

    @Test
    void testCurrentPrice_noSalePriceNoBids() {
        Sale sale = new Sale();
        sale.setStartingPrice(50);

        assertEquals(50, sale.getCurrentPrice());
    }
    @Test
    void testCurrentPrice_SalePriceNoBids() {
        Sale sale = new Sale();
        sale.setStartingPrice(50);
        sale.setSalePrice(100);

        assertEquals(100, sale.getCurrentPrice());
    }
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
