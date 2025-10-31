package fr.rougeux.projet.auction.bo;

import fr.rougeux.projet.auction.dto.bo.BidDto;
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
     * Vérifie si cette enchère est supérieure à un montant donné.
     * @param amount montant à comparer
     * @return vrai si bidAmount est supérieur à amount
     */
    public boolean isHigherThan(int amount) {
        return bidAmount > amount;
    }

    /**
     * Vérifie si cette enchère est la plus élevée pour la vente.
     * @return vrai si bidAmount correspond au montant actuel le plus élevé
     */
    public boolean isHighestBid() {
        if (sale == null || sale.getBids() == null) return false;
        return bidAmount == sale.getCurrentPrice();
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
