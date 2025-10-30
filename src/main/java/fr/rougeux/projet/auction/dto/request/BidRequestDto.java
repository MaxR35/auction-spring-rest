package fr.rougeux.projet.auction.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class BidRequestDto {

    @NotNull(message = "Sell Id is required")
    private Long saleId;

    @NotNull(message = "User Id is required")
    private Long userId;

    @Positive(message = "Bid must be higher than zero")
    private int bidAmount;

    private LocalDateTime bidTime = LocalDateTime.now();

    public BidRequestDto() {}

    public BidRequestDto(Long saleId, Long userId, int bidAmount, LocalDateTime bidTime) {
        this.saleId = saleId;
        this.userId = userId;
        this.bidAmount = bidAmount;
        this.bidTime = bidTime != null ? bidTime : LocalDateTime.now();
    }

    public Long getSaleId() { return saleId; }
    public void setSaleId(Long saleId) { this.saleId = saleId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public int getBidAmount() { return bidAmount; }
    public void setBidAmount(int bidAmount) { this.bidAmount = bidAmount; }

    public LocalDateTime getBidTime() { return bidTime; }
    public void setBidTime(LocalDateTime bidTime) { this.bidTime = bidTime; }
}
