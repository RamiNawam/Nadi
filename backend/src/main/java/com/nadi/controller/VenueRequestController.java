package com.nadi.controller;

import com.nadi.model.Money;
import com.nadi.model.SportType;
import com.nadi.model.VenueRequest;
import com.nadi.service.VenueRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/venue-requests")
@CrossOrigin(origins = "*")
public class VenueRequestController {

    @Autowired
    private VenueRequestService venueRequestService;

    @PostMapping
    public ResponseEntity<VenueRequest> submitVenueRequest(@RequestBody Map<String, Object> request) {
        VenueRequest venueRequest = venueRequestService.submitVenueRequest(
            (String) request.get("venueName"),
            (String) request.get("address"),
            (Boolean) request.getOrDefault("cafeteriaAvailable", false),
            new HashMap<>(),
            new HashMap<>(),
            new HashMap<>()
        );
        return ResponseEntity.ok(venueRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueRequest> getVenueRequest(@PathVariable String id) {
        return venueRequestService.findById(java.util.UUID.fromString(id))
            .map(venueRequest -> ResponseEntity.ok(venueRequest))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<VenueRequest>> getAllRequests() {
        return ResponseEntity.ok(venueRequestService.getAllRequests());
    }

    @GetMapping("/pending")
    public ResponseEntity<List<VenueRequest>> getPendingRequests() {
        return ResponseEntity.ok(venueRequestService.getPendingRequests());
    }

    @GetMapping("/approved")
    public ResponseEntity<List<VenueRequest>> getApprovedRequests() {
        return ResponseEntity.ok(venueRequestService.getApprovedRequests());
    }

    @GetMapping("/rejected")
    public ResponseEntity<List<VenueRequest>> getRejectedRequests() {
        return ResponseEntity.ok(venueRequestService.getRejectedRequests());
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<Void> approveRequest(@PathVariable String id) {
        venueRequestService.approveRequest(java.util.UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectRequest(@PathVariable String id, @RequestBody Map<String, String> request) {
        String reason = request.getOrDefault("reason", "No reason provided");
        venueRequestService.rejectRequest(java.util.UUID.fromString(id), reason);
        return ResponseEntity.ok().build();
    }
}

