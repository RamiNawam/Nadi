package com.nadi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("courts")
public class Court {
    @Id
    private UUID id;
    private String label;
    private SportType sport;
    private int capacity;
    private Money pricePerHour;

    // UML: Court "1" --> "1" CalendarAvailability
    @DBRef(lazy = true)
    private CalendarAvailability calendar;
}

