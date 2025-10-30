package fr.rougeux.projet.auction.dto.response;

import fr.rougeux.projet.auction.dto.bo.SaleDto;
import fr.rougeux.projet.auction.dto.bo.UserDto;

public record BidResponseDto(SaleDto sale, UserDto user) {}
