package fr.rougeux.projet.auction.repository;

import fr.rougeux.projet.auction.bo.User;

public interface UserDao {

    User getUserByEmail(String email);
}
