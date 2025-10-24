package com.nadi.reservation.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Reservation entity for court bookings
 * Uses compound index for overlap checking
 */
@Document(collection = "reservations")
@CompoundIndex(name = "court_time_idx", def = "{'courtId': 1, 'startTime': 1, 'endTime': 1}")
public class Reservation {
    
    @Id
    private String id;
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "Court ID is required")
    private String courtId;
    
    @NotNull
    @Future(message = "Start time must be in the future")
    private Instant startTime;
    
    @NotNull
    @Future(message = "End time must be in the future")
    private Instant endTime;
    
    @Min(1)
    private int playersCount;
    
    @NotNull
    @DecimalMin(value = "0.0", message = "Price must be non-negative")
    private BigDecimal priceTotal;
    
    private String currency = "USD";
    
    @NotNull
    private ReservationStatus status = ReservationStatus.HELD;
    
    private Instant createdAt = Instant.now();
    
    @Indexed
    private Instant holdExpiresAt;
    
    public enum ReservationStatus {
        HELD, CONFIRMED, CANCELLED, EXPIRED
    }
    
    // Constructors
    public Reservation() {}
    
    public Reservation(String userId, String courtId, Instant startTime, Instant endTime, 
                      int playersCount, BigDecimal priceTotal) {
        this.userId = userId;
        this.courtId = courtId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.playersCount = playersCount;
        this.priceTotal = priceTotal;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getCourtId() { return courtId; }
    public void setCourtId(String courtId) { this.courtId = courtId; }
    
    public Instant getStartTime() { return startTime; }
    public void setStartTime(Instant startTime) { this.startTime = startTime; }
    
    public Instant getEndTime() { return endTime; }
    public void setEndTime(Instant endTime) { this.endTime = endTime; }
    
    public int getPlayersCount() { return playersCount; }
    public void setPlayersCount(int playersCount) { this.playersCount = playersCount; }
    
    public BigDecimal getPriceTotal() { return priceTotal; }
    public void setPriceTotal(BigDecimal priceTotal) { this.priceTotal = priceTotal; }
    
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    
    public ReservationStatus getStatus() { return status; }
    public void setStatus(ReservationStatus status) { this.status = status; }
    
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    
    public Instant getHoldExpiresAt() { return holdExpiresAt; }
    public void setHoldExpiresAt(Instant holdExpiresAt) { this.holdExpiresAt = holdExpiresAt; }
}
