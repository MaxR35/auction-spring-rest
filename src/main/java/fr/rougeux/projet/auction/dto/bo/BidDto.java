package fr.rougeux.projet.auction.dto.bo;

import java.time.LocalDateTime;

public class BidDto {

    private long bidId;
    private LocalDateTime bidTime;
    private int bidAmount;
    private SaleDto sale;
    private UserDto user;

    public long getBidId() { return bidId; }
    public void setBidId(long bidId) { this.bidId = bidId; }

    public LocalDateTime getBidTime() { return bidTime; }
    public void setBidTime(LocalDateTime bidTime) { this.bidTime = bidTime; }

    public int getBidAmount() { return bidAmount; }
    public void setBidAmount(int bidAmount) { this.bidAmount = bidAmount; }

    public SaleDto getSale() { return sale; }
    public void setSale(SaleDto sale) { this.sale = sale; }

    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }
}
