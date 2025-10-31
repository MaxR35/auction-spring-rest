package fr.rougeux.projet.auction.bo;

import fr.rougeux.projet.auction.dto.bo.BidDto;
import fr.rougeux.projet.auction.exception.BusinessException;

import java.time.LocalDateTime;

/**
 * BO représentant une enchère réalisée par un utilisateur sur une vente.
 */
public class Bid {

    /** Identifiant unique de l'enchère */
    private long bidId;

    /** Date et heure de l'enchère */
    private LocalDateTime bidTime;

    /** Montant de l'enchère */
    private int bidAmount;

    /** Vente concernée par l'enchère */
    private Sale sale;

    /** Utilisateur ayant placé l'enchère */
    private User user;

    // =========================
    // Constructors
    // =========================

    /**
     * Constructeur par défaut
     */
    public Bid() {}

    /**
     * Constructeur de copie pour cloner une enchère.
     *
     * @param source instance de Bid à cloner
     */
    public Bid(Bid source) {
        this.bidId = source.bidId;
        this.bidTime = source.bidTime;
        this.bidAmount = source.bidAmount;

        this.sale = (source.sale != null) ? new Sale(source.sale) : null;
        this.user = (source.user != null) ? new User(source.user) : null;
    }

    // =========================
    // Getters et Setters
    // =========================

    public long getBidId() {
        return bidId;
    }

    public void setBidId(long bidId) {
        this.bidId = bidId;
    }

    public LocalDateTime getBidTime() {
        return bidTime;
    }

    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }

    public int getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(int bidAmount) {
        this.bidAmount = bidAmount;
    }

    public Sale getSale() {
        return sale;
    }

    public void setSale(Sale sale) {
        this.sale = sale;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // =========================
    // Logique métier
    // =========================

    /**
     * Vérifie que l'enchère et l'utilisateur associés ne sont pas null.
     *
     * @throws BusinessException si l'enchère ou l'utilisateur est null
     */
    public void checkNotNull() {
        if(this.sale == null)
            throw new BusinessException("bid.sale.undefined");
        if(this.user == null)
            throw new BusinessException("bid.user.undefined");
    }

    /**
     * Vérifie que le montant de l'enchère est supérieur au prix courant de la vente.
     *
     * @throws BusinessException si l'enchère est inférieure ou égale au prix actuel
     */
    public void checkHigherBid() {
        int currentPrice = this.sale.getCurrentPrice();
        if(this.bidAmount <= currentPrice) {
            throw new BusinessException("bid.amount.tooLow");
        }
    }

    /**
     * Vérifie que l'utilisateur qui place l'enchère n'est pas le vendeur de l'objet.
     *
     * @throws BusinessException si l'utilisateur est le vendeur
     */
    public void checkUserNotSeller() {
        if(this.sale.getSeller().getUserId() == this.user.getUserId()) {
            throw new BusinessException("bid.user.isSeller");
        }
    }

    /**
     * Vérifie que l'utilisateur a suffisamment de crédits pour placer l'enchère.
     * Si l'utilisateur a déjà une enchère sur cette vente, seule la différence est vérifiée.
     *
     * @throws BusinessException si l'utilisateur n'a pas assez de crédits
     */
    public void checkUserCanBid() {
        int previousBid = 0;
        if (sale.getBids() != null) {
            previousBid = sale.getBids().stream()
                    .filter(b -> b.getUser().getUserId() == this.user.getUserId())
                    .mapToInt(Bid::getBidAmount)
                    .max()
                    .orElse(0);
        }

        int diff = bidAmount - previousBid;
        this.user.canBid(diff);
    }

    /**
     * Vérifie que la vente est encore en cours.
     *
     * @throws BusinessException si la vente est terminée
     */
    public void checkSaleStatus() {
        if(!this.sale.isOngoing()) {
            throw new BusinessException("bid.sale.over");
        }
    }

    /**
     * Valide toutes les règles métiers pour placer une enchère.
     *
     * @throws BusinessException si une des validations échoue
     */
    public void validateBid() {
        checkNotNull();
        // checkSaleStatus(); // TODO: à réactiver lorsque faker regénérera des ventes en cours
        checkHigherBid();
        checkUserCanBid();
    }

    // =========================
    // Conversion vers DTO
    // =========================

    /**
     * Convertit ce BO en BidDTO pour transfert vers le controller ou l'API.
     * @return BidDTO correspondant
     */
    public BidDto toDTO() {
        BidDto dto = new BidDto();
        dto.setBidId(this.bidId);
        dto.setBidTime(this.bidTime);
        dto.setBidAmount(this.bidAmount);
        dto.setSale(this.sale != null ? this.sale.toDTO() : null);
        dto.setUser(this.user != null ? this.user.toDTO() : null);
        return dto;
    }
}
