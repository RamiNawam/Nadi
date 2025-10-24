package com.nadi.auth.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;

/**
 * API key entity for developer access
 */
@Document(collection = "api_keys")
public class ApiKey {
    
    @Id
    private String id;
    
    @NotBlank(message = "User ID is required")
    private String userId;
    
    @NotBlank(message = "API key is required")
    private String key;
    
    @Min(1)
    private int rateLimit = 1000; // requests per hour
    
    private LocalDateTime createdAt = LocalDateTime.now();
    
    private LocalDateTime lastUsed;
    
    // Constructors
    public ApiKey() {}
    
    public ApiKey(String userId, String key, int rateLimit) {
        this.userId = userId;
        this.key = key;
        this.rateLimit = rateLimit;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
    
    public int getRateLimit() { return rateLimit; }
    public void setRateLimit(int rateLimit) { this.rateLimit = rateLimit; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getLastUsed() { return lastUsed; }
    public void setLastUsed(LocalDateTime lastUsed) { this.lastUsed = lastUsed; }
}
