package fr.rougeux.projet.auction.bo;

import fr.rougeux.projet.auction.dto.bo.SaleDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * BO représentant une vente aux enchères.
 * Contient les informations sur la vente et ses objets liés.
 */
public class Sale {

    /** Identifiant unique de la vente */
    private long saleId;

    /** Date et heure de début de la vente */
    private LocalDateTime startingDate;

    /** Date et heure de fin de la vente */
    private LocalDateTime endingDate;

    /** Prix de départ de la vente */
    private int startingPrice;

    /** Prix actuel ou prix final de la vente */
    private int salePrice;

    /** Vendeur de l'objet */
    private User seller;

    /** Objet mis en vente */
    private Item item;

    /** Liste des enchères associées */
    private List<Bid> bids;

    // =========================
    // Getters et Setters
    // =========================

    public long getSaleId() {
        return saleId;
    }

    public void setSaleId(long saleId) {
        this.saleId = saleId;
    }

    public LocalDateTime getStartingDate() {
        return startingDate;
    }

    public void setStartingDate(LocalDateTime startingDate) {
        this.startingDate = startingDate;
    }

    public LocalDateTime getEndingDate() {
        return endingDate;
    }

    public void setEndingDate(LocalDateTime endingDate) {
        this.endingDate = endingDate;
    }

    public int getStartingPrice() {
        return startingPrice;
    }

    public void setStartingPrice(int startingPrice) {
        this.startingPrice = startingPrice;
    }

    public int getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice;
    }

    public User getSeller() {
        return seller;
    }

    public void setSeller(User seller) {
        this.seller = seller;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    // =========================
    // Logique métier
    // =========================

    /**
     * Vérifie si la vente est toujours en cours.
     * @return vrai si la date de fin est après maintenant
     */
    public boolean isOngoing() {
        return endingDate.isAfter(LocalDateTime.now());
    }

    /**
     * Retourne le statut de la vente.
     * @return "ONGOING" si en cours, "OVER" sinon
     */
    public String getStatus() {
        return isOngoing() ? "ONGOING" : "OVER";
    }

    /**
     * Retourne l'enchère la plus élevée.
     * @return montant de la meilleure enchère, ou 0 si aucune
     */
    public int getCurrentBid() {
        if (bids == null || bids.isEmpty()) return this.startingPrice;
        return bids.stream()
                .mapToInt(Bid::getBidAmount)
                .max()
                .orElse(0);
    }

    // =========================
    // Conversion vers DTO
    // =========================

    /**
     * Convertit ce BO en SaleDto pour transfert vers le controller ou l'API.
     * @return SaleDto correspondant à ce BO
     */
    public SaleDto toDTO() {
        SaleDto dto = new SaleDto();
        dto.setSaleId(this.saleId);
        dto.setStartingDate(this.startingDate);
        dto.setEndingDate(this.endingDate);
        dto.setStartingPrice(this.startingPrice);
        dto.setSalePrice(this.salePrice);
        dto.setStatus(this.getStatus());
        dto.setCurrentBid(this.getCurrentBid());
        dto.setSeller(this.seller != null ? this.seller.toDTO() : null);
        dto.setItem(this.item != null ? this.item.toDTO() : null);
        if (this.bids != null) {
            dto.setBidLst(this.bids.stream()
                    .map(Bid::toDTO)
                    .toList());
        }
        return dto;
    }
}
