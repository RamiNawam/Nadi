package com.nadi.sport.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * Sport entity defining available sports
 */
@Document(collection = "sports")
public class Sport {
    
    @Id
    private String id;
    
    @NotNull
    private SportType name;
    
    @Min(1)
    private int minPlayers;
    
    @Min(1)
    private int maxPlayers;
    
    @Min(15)
    private int defaultSlotMinutes = 60;
    
    public enum SportType {
        FOOTBALL, BASKETBALL, PADEL, TENNIS
    }
    
    // Constructors
    public Sport() {}
    
    public Sport(SportType name, int minPlayers, int maxPlayers, int defaultSlotMinutes) {
        this.name = name;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
        this.defaultSlotMinutes = defaultSlotMinutes;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public SportType getName() { return name; }
    public void setName(SportType name) { this.name = name; }
    
    public int getMinPlayers() { return minPlayers; }
    public void setMinPlayers(int minPlayers) { this.minPlayers = minPlayers; }
    
    public int getMaxPlayers() { return maxPlayers; }
    public void setMaxPlayers(int maxPlayers) { this.maxPlayers = maxPlayers; }
    
    public int getDefaultSlotMinutes() { return defaultSlotMinutes; }
    public void setDefaultSlotMinutes(int defaultSlotMinutes) { this.defaultSlotMinutes = defaultSlotMinutes; }
}
