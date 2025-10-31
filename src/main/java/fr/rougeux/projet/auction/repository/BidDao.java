package fr.rougeux.projet.auction.repository;

import fr.rougeux.projet.auction.bo.Bid;

import java.util.List;

public interface BidDao {

    List<Bid> readAll(long saleId);
}
