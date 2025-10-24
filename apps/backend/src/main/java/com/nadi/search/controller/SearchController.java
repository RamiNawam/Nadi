package com.nadi.search.controller;

import com.nadi.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * Search endpoints for venue discovery
 */
@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:3000")
public class SearchController {

    @Autowired
    private SearchService searchService;

    /**
     * Get all available sports
     */
    @GetMapping("/sports")
    public ResponseEntity<?> getSports() {
        // TODO: Return list of available sports
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Search venues with filters
     */
    @GetMapping("/venues")
    public ResponseEntity<?> searchVenues(
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) String sport,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Integer players) {
        // TODO: Implement venue search with geospatial and filter support
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Get venue details
     */
    @GetMapping("/venues/{id}")
    public ResponseEntity<?> getVenueDetails(@PathVariable String id) {
        // TODO: Return venue with courts and pricing
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Check court availability
     */
    @GetMapping("/courts/{id}/availability")
    public ResponseEntity<?> getCourtAvailability(
            @PathVariable String id,
            @RequestParam String date) {
        // TODO: Return available time slots for court on given date
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
