package com.nadi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Data
@EqualsAndHashCode(callSuper = true)
@TypeAlias("venue")
public class VenueAccount extends Account {
    private String companyName;

    // UML: VenueAccount "1" --> "0..1" Venue : owns
    @DBRef(lazy = true)
    private Venue venue;

    public void requestVenue() {
    }

    public void modifyReservation(String reservationId, Object changes) {
    }
}

