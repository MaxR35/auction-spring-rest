package fr.rougeux.projet.auction.dto.bo;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO représentant une vente.
 */
public class SaleDto {

    private long saleId;
    private LocalDateTime startingDate;
    private LocalDateTime endingDate;
    private int startingPrice;
    private int salePrice;
    private String status;
    private int currentBid;
    private UserDto seller;
    private ItemDto item;
    private List<BidDto> bidLst;

    public long getSaleId() { return saleId; }
    public void setSaleId(long saleId) { this.saleId = saleId; }

    public LocalDateTime getStartingDate() { return startingDate; }
    public void setStartingDate(LocalDateTime startingDate) { this.startingDate = startingDate; }

    public LocalDateTime getEndingDate() { return endingDate; }
    public void setEndingDate(LocalDateTime endingDate) { this.endingDate = endingDate; }

    public int getStartingPrice() { return startingPrice; }
    public void setStartingPrice(int startingPrice) { this.startingPrice = startingPrice; }

    public int getSalePrice() { return salePrice; }
    public void setSalePrice(int salePrice) { this.salePrice = salePrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCurrentBid() { return currentBid; }
    public void setCurrentBid(int currentBid) { this.currentBid = currentBid; }

    public UserDto getSeller() { return seller; }
    public void setSeller(UserDto seller) { this.seller = seller; }

    public ItemDto getItem() { return item; }
    public void setItem(ItemDto item) { this.item = item; }

    public List<BidDto> getBidLst() { return bidLst; }
    public void setBidLst(List<BidDto> bidLst) { this.bidLst = bidLst; }
}
