package com.nadi.controller;

import com.nadi.model.SearchCriteria;
import com.nadi.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Search endpoints
 */
@RestController
@RequestMapping("/search")
@CrossOrigin(origins = "http://localhost:3000")
public class SearchController {

    @Autowired
    private SearchService searchService;

    // TODO: Implement advanced search endpoint
    @PostMapping("/venues")
    public ResponseEntity<?> searchVenues(@RequestBody SearchCriteria criteria) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // TODO: Implement nearby venues endpoint
    @GetMapping("/venues/nearby")
    public ResponseEntity<?> findNearbyVenues(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // TODO: Implement sport-specific search endpoint
    @GetMapping("/venues/sport")
    public ResponseEntity<?> searchBySport(
            @RequestParam String sportType,
            @RequestParam(required = false) String location) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // TODO: Implement availability search endpoint
    @GetMapping("/venues/available")
    public ResponseEntity<?> searchAvailableVenues(
            @RequestParam String sportType,
            @RequestParam String date,
            @RequestParam String timeSlot) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}

