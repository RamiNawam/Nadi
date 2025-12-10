package com.nadi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("venues")
public class Venue {
    @Id
    private String id; // Changed to String to handle both ObjectId and UUID
    private String name;
    private String address;
    private boolean cafeteriaAvailable;
    private GeoPoint location;
}

