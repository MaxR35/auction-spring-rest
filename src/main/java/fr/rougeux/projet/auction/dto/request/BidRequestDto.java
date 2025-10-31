package fr.rougeux.projet.auction.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class BidRequestDto {

    @NotNull(message = "Sell is required")
    private long saleId;

    @Positive(message = "Bid must be higher than zero")
    private int bidAmount;

    private LocalDateTime bidTime = LocalDateTime.now();

    public BidRequestDto() {}

    public BidRequestDto(long saleId, int bidAmount, LocalDateTime bidTime) {
        this.saleId = saleId;
        this.bidAmount = bidAmount;
        this.bidTime = bidTime != null ? bidTime : LocalDateTime.now();
    }

    public Long getSaleId() { return saleId; }
    public void setSaleId(Long saleId) { this.saleId = saleId; }

    public int getBidAmount() { return bidAmount; }
    public void setBidAmount(int bidAmount) { this.bidAmount = bidAmount; }

    public LocalDateTime getBidTime() { return bidTime; }
    public void setBidTime(LocalDateTime bidTime) { this.bidTime = bidTime; }
}
