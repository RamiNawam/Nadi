package com.nadi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("venue_requests")
public class VenueRequest {
    @Id
    private UUID id;
    private OffsetDateTime submittedAt;
    private String status;
    private String venueName;
    private String address;
    private boolean cafeteriaAvailable;

    private Map<SportType, Integer> numberOfCourts;
    private Map<SportType, Integer> playersPerCourt;
    private Map<SportType, Money> pricePerHour;

    public void approve() {
        this.status = "APPROVED";
    }

    public void reject(String reason) {
        this.status = "REJECTED";
    }
}

