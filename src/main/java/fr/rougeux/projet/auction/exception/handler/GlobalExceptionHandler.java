package fr.rougeux.projet.auction.exception.handler;

import fr.rougeux.projet.auction.dto.error.ErrorDto;
import fr.rougeux.projet.auction.exception.BusinessException;
import fr.rougeux.projet.auction.exception.NotFoundException;
import fr.rougeux.projet.auction.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Gestionnaire global des exceptions pour l'API REST.
 * <p>
 * Ce {@link RestControllerAdvice} intercepte les exceptions levées dans tous les controllers
 * et renvoie une réponse HTTP appropriée avec un message clair.
 * </p>
 *
 * @author Rougeux Max
 * @version 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Crée un objet {@link ErrorDto} avec le statut HTTP, le message d'erreur et le timestamp actuel.
     *
     * <p>Cette méthode est utilisée pour uniformiser le format des réponses d'erreur
     * renvoyées par le {@link GlobalExceptionHandler}.</p>
     *
     * @param status  le statut HTTP correspondant à l'erreur (ex : 400, 404, 500)
     * @param message le message décrivant l'erreur
     * @return un {@link ErrorDto} contenant le statut, le message et le timestamp
     */
    private ErrorDto buildError(HttpStatus status, String message) {
        return new ErrorDto(status.value(), message, System.currentTimeMillis());
    }

    /**
     * Gère les exceptions métier personnalisées.
     *
     * @param e l'exception métier levée
     * @return ResponseEntity avec status 400 et message de l'exception
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> handleBadRequest(BusinessException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, e.getMessage()));
    }

    /**
     * Gère les exceptions métier lié à un retour vide de la db.
     *
     * @param e l'exception métier levée
     * @return ResponseEntity avec status 404 et message de l'exception
     */
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> handleNotFound(NotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    /**
     * Gère les exceptions d'accès refusé.
     *
     * @param e l'exception levée lorsqu'un utilisateur n'a pas les droits requis
     * @return ResponseEntity avec status 403 et message de l'exception
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDto> handleForbidden(AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildError(HttpStatus.FORBIDDEN, e.getMessage()));
    }

    /**
     * Gère les exceptions d'authentification non autorisée.
     *
     * @param e l'exception levée lorsqu'un utilisateur n'est pas authentifié
     * @return ResponseEntity avec status 401 et message de l'exception
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDto> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildError(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    /**
     * Gère les exceptions liées à de mauvaises informations d'identification.
     *
     * @param e l'exception levée lors d'une tentative de connexion avec un mauvais mot de passe ou username
     * @return ResponseEntity avec status 401 et message de l'exception
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildError(HttpStatus.UNAUTHORIZED, e.getMessage()));
    }

    /**
     * Gère toutes les exceptions non prévues.
     *
     * @param e l'exception levée
     * @return ResponseEntity avec status 500 et message de l'exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGeneral(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
    }
}
