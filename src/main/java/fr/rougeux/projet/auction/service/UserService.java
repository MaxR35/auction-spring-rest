package fr.rougeux.projet.auction.service;

import fr.rougeux.projet.auction.dto.bo.UserDto;

public interface UserService {

    UserDto findByEmail(String email);
}
