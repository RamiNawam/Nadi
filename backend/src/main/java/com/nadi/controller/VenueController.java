package com.nadi.controller;

import com.nadi.model.Venue;
import com.nadi.model.Court;
import com.nadi.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Venue endpoints
 */
@RestController
@RequestMapping("/venues")
@CrossOrigin(origins = "http://localhost:3000")
public class VenueController {

    @Autowired
    private VenueService venueService;

    // TODO: Implement venue search by location endpoint
    @GetMapping("/search/location")
    public ResponseEntity<?> searchVenuesByLocation(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // TODO: Implement venue search by sport endpoint
    @GetMapping("/search/sport")
    public ResponseEntity<?> searchVenuesBySport(@RequestParam String sportType) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // TODO: Implement get venue details endpoint
    @GetMapping("/{venueId}")
    public ResponseEntity<Venue> getVenueDetails(@PathVariable Long venueId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // TODO: Implement get available courts endpoint
    @GetMapping("/{venueId}/courts/available")
    public ResponseEntity<?> getAvailableCourts(
            @PathVariable Long venueId,
            @RequestParam String date,
            @RequestParam String timeSlot) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // TODO: Implement create venue endpoint (admin only)
    @PostMapping
    public ResponseEntity<Venue> createVenue(@RequestBody Venue venue) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

