package fr.rougeux.projet.auction.bo;

import fr.rougeux.projet.auction.dto.bo.SaleDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * BO représentant une vente aux enchères.
 * Contient les informations sur la vente et ses objets liés.
 */
public class Sale {

    /**
     * Identifiant unique de la vente
     */
    private long saleId;

    /**
     * Date et heure de début de la vente
     */
    private LocalDateTime startingDate;

    /**
     * Date et heure de fin de la vente
     */
    private LocalDateTime endingDate;

    /**
     * Prix de départ de la vente
     */
    private int startingPrice;

    /**
     * Prix actuel ou prix final de la vente
     */
    private int salePrice;

    /**
     * Vendeur de l'objet
     */
    private User seller;

    /**
     * Objet mis en vente
     */
    private Item item;

    /**
     * Liste des enchères associées
     */
    private List<Bid> bids;

    // =========================
    // Constructors
    // =========================

    /**
     * Constructeur par défaut
     */
    public Sale() {
    }

    /**
     * Constructeur de copie pour cloner une vente
     *
     * @param source instance de Sale à cloner
     */
    public Sale(Sale source) {
        this.saleId = source.saleId;
        this.startingDate = source.startingDate;
        this.endingDate = source.endingDate;
        this.startingPrice = source.startingPrice;
        this.salePrice = source.salePrice;

        this.seller = (source.seller != null) ? new User(source.seller) : null;
        this.item = (source.item != null) ? new Item(source.item) : null;
        if (source.bids != null) {
            this.bids = new ArrayList<>();
            for (Bid bid : source.bids) {
                this.bids.add(new Bid(bid));
            }
        }
    }

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
     *
     * @return vrai si la date de fin est après maintenant
     */
    public boolean isOngoing() {
        return endingDate.isAfter(LocalDateTime.now());
    }

    /**
     * Retourne le statut de la vente.
     *
     * @return "ONGOING" si en cours, "OVER" sinon
     */
    public String getStatus() {
        return isOngoing() ? "ONGOING" : "OVER";
    }

    /**
     * Retourne le prix actuel à afficher :
     * - Si salePrice est défini (> 0), c’est le prix actuel (ou final)
     * - Sinon, c’est le prix de départ
     */
    public int getCurrentPrice() {
        if (bids != null && !bids.isEmpty()) {
            return bids.stream()
                    .mapToInt(Bid::getBidAmount)
                    .max()
                    .orElse(startingPrice);
        }

        return (salePrice > 0) ? salePrice : startingPrice;
    }

    // =========================
    // Conversion vers DTO
    // =========================

    /**
     * Convertit ce BO en SaleDto pour transfert vers le controller ou l'API.
     *
     * @return SaleDto correspondant à ce BO
     */
    public SaleDto toDTO() {
        SaleDto dto = new SaleDto();
        dto.setSaleId(this.saleId);
        dto.setStartingDate(this.startingDate);
        dto.setEndingDate(this.endingDate);
        dto.setStartingPrice(this.startingPrice);
        dto.setSalePrice(this.getCurrentPrice());
        dto.setStatus(this.getStatus());
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
