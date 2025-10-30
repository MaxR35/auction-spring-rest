package fr.rougeux.projet.auction.exception;

public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(message);
    }
}
