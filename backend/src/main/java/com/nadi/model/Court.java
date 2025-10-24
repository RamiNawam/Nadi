package com.nadi.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * Court domain model representing individual sports courts within venues.
 * 
 * This entity will contain court information such as:
 * - Court type (football, basketball, padel, tennis)
 * - Court specifications and dimensions
 * - Pricing information
 * - Availability status
 */
@Entity
@Table(name = "courts")
public class Court {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Court name is required")
    @Size(min = 2, max = 100, message = "Court name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false)
    private String name;
    
    @NotBlank(message = "Sport type is required")
    @Size(max = 50, message = "Sport type must not exceed 50 characters")
    @Column(name = "sport_type", nullable = false)
    private String sportType;
    
    @Size(max = 200, message = "Description must not exceed 200 characters")
    @Column(name = "description")
    private String description;
    
    @Column(name = "price_per_hour", nullable = false)
    private Double pricePerHour;
    
    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable = true;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;
    
    // Default constructor
    public Court() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Constructor with required fields
    public Court(String name, String sportType, Double pricePerHour, Venue venue) {
        this();
        this.name = name;
        this.sportType = sportType;
        this.pricePerHour = pricePerHour;
        this.venue = venue;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSportType() {
        return sportType;
    }
    
    public void setSportType(String sportType) {
        this.sportType = sportType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Double getPricePerHour() {
        return pricePerHour;
    }
    
    public void setPricePerHour(Double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }
    
    public Boolean getIsAvailable() {
        return isAvailable;
    }
    
    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Venue getVenue() {
        return venue;
    }
    
    public void setVenue(Venue venue) {
        this.venue = venue;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

