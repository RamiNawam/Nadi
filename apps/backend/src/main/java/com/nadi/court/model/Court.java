package com.nadi.court.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

/**
 * Court entity representing individual sports courts within venues
 */
@Document(collection = "courts")
public class Court {
    
    @Id
    private String id;
    
    @NotBlank(message = "Venue ID is required")
    private String venueId;
    
    @NotBlank(message = "Sport ID is required")
    private String sportId;
    
    @NotBlank(message = "Court name is required")
    @Size(min = 2, max = 100, message = "Court name must be between 2 and 100 characters")
    private String name;
    
    private String surface;
    
    @Min(1)
    private int maxPlayers;
    
    private boolean isActive = true;
    
    // Constructors
    public Court() {}
    
    public Court(String venueId, String sportId, String name, String surface, int maxPlayers) {
        this.venueId = venueId;
        this.sportId = sportId;
        this.name = name;
        this.surface = surface;
        this.maxPlayers = maxPlayers;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getVenueId() { return venueId; }
    public void setVenueId(String venueId) { this.venueId = venueId; }
    
    public String getSportId() { return sportId; }
    public void setSportId(String sportId) { this.sportId = sportId; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSurface() { return surface; }
    public void setSurface(String surface) { this.surface = surface; }
    
    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}
