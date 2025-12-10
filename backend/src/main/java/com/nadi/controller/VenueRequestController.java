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
@RequestMapping("/v1/venue-requests")
@CrossOrigin(origins = "*")
public class VenueRequestController {

    @Autowired
    private VenueRequestService venueRequestService;

    @PostMapping
    public ResponseEntity<?> submitVenueRequest(@RequestBody Map<String, Object> request) {
        try {
            String venueAccountIdStr = (String) request.get("venueAccountId");
            if (venueAccountIdStr == null) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "venueAccountId is required");
                return ResponseEntity.badRequest().body(error);
            }
            
            java.util.UUID venueAccountId = java.util.UUID.fromString(venueAccountIdStr);
            
            // Convert Maps with String keys to Maps with SportType enum keys
            @SuppressWarnings("unchecked")
            Map<String, Object> numberOfCourtsMap = (Map<String, Object>) request.getOrDefault("numberOfCourts", new HashMap<>());
            @SuppressWarnings("unchecked")
            Map<String, Object> playersPerCourtMap = (Map<String, Object>) request.getOrDefault("playersPerCourt", new HashMap<>());
            @SuppressWarnings("unchecked")
            Map<String, Object> pricePerHourMap = (Map<String, Object>) request.getOrDefault("pricePerHour", new HashMap<>());
            
            Map<SportType, Integer> numberOfCourts = convertToSportTypeMap(numberOfCourtsMap);
            Map<SportType, Integer> playersPerCourt = convertToSportTypeMap(playersPerCourtMap);
            Map<SportType, Money> pricePerHour = convertToMoneyMap(pricePerHourMap);
            
            VenueRequest venueRequest = venueRequestService.submitVenueRequest(
                venueAccountId,
                (String) request.get("venueName"),
                (String) request.get("address"),
                (Boolean) request.getOrDefault("cafeteriaAvailable", false),
                numberOfCourts,
                playersPerCourt,
                pricePerHour
            );
            return ResponseEntity.ok(venueRequest);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid request format: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    private Map<SportType, Integer> convertToSportTypeMap(Map<String, Object> stringMap) {
        Map<SportType, Integer> result = new HashMap<>();
        if (stringMap != null) {
            for (Map.Entry<String, Object> entry : stringMap.entrySet()) {
                try {
                    SportType sportType = SportType.valueOf(entry.getKey().toUpperCase());
                    Object value = entry.getValue();
                    if (value instanceof Number) {
                        result.put(sportType, ((Number) value).intValue());
                    } else if (value instanceof String) {
                        result.put(sportType, Integer.parseInt((String) value));
                    }
                } catch (IllegalArgumentException e) {
                    // Skip invalid sport types
                }
            }
        }
        return result;
    }

    private Map<SportType, Money> convertToMoneyMap(Map<String, Object> stringMap) {
        Map<SportType, Money> result = new HashMap<>();
        if (stringMap != null) {
            for (Map.Entry<String, Object> entry : stringMap.entrySet()) {
                try {
                    SportType sportType = SportType.valueOf(entry.getKey().toUpperCase());
                    Object value = entry.getValue();
                    if (value instanceof Map) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> moneyMap = (Map<String, Object>) value;
                        Money money = new Money();
                        Object amount = moneyMap.get("amount");
                        if (amount != null) {
                            if (amount instanceof Number) {
                                money.setAmount(java.math.BigDecimal.valueOf(((Number) amount).doubleValue()));
                            } else if (amount instanceof String) {
                                money.setAmount(new java.math.BigDecimal((String) amount));
                            }
                        }
                        Object currency = moneyMap.get("currency");
                        if (currency != null) {
                            money.setCurrency(currency.toString());
                        } else {
                            money.setCurrency("USD"); // Default currency
                        }
                        result.put(sportType, money);
                    }
                } catch (IllegalArgumentException e) {
                    // Skip invalid sport types
                }
            }
        }
        return result;
    }

    @GetMapping("/my-requests/{venueAccountId}")
    public ResponseEntity<List<VenueRequest>> getMyRequests(@PathVariable String venueAccountId) {
        return ResponseEntity.ok(venueRequestService.getRequestsByVenueAccount(java.util.UUID.fromString(venueAccountId)));
    }

    @GetMapping("/my-requests/{venueAccountId}/latest")
    public ResponseEntity<?> getMyLatestRequest(@PathVariable String venueAccountId) {
        return venueRequestService.getLatestRequestByVenueAccount(java.util.UUID.fromString(venueAccountId))
            .map(request -> ResponseEntity.ok(request))
            .orElse(ResponseEntity.notFound().build());
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVenueRequest(@PathVariable String id, @RequestBody Map<String, Object> request) {
        try {
            java.util.UUID requestId = java.util.UUID.fromString(id);
            
            // Convert Maps with String keys to Maps with SportType enum keys
            @SuppressWarnings("unchecked")
            Map<String, Object> numberOfCourtsMap = (Map<String, Object>) request.getOrDefault("numberOfCourts", new HashMap<>());
            @SuppressWarnings("unchecked")
            Map<String, Object> playersPerCourtMap = (Map<String, Object>) request.getOrDefault("playersPerCourt", new HashMap<>());
            @SuppressWarnings("unchecked")
            Map<String, Object> pricePerHourMap = (Map<String, Object>) request.getOrDefault("pricePerHour", new HashMap<>());
            
            Map<SportType, Integer> numberOfCourts = convertToSportTypeMap(numberOfCourtsMap);
            Map<SportType, Integer> playersPerCourt = convertToSportTypeMap(playersPerCourtMap);
            Map<SportType, Money> pricePerHour = convertToMoneyMap(pricePerHourMap);
            
            VenueRequest updatedRequest = venueRequestService.updateVenueRequest(
                requestId,
                (String) request.get("venueName"),
                (String) request.get("address"),
                (Boolean) request.getOrDefault("cafeteriaAvailable", false),
                numberOfCourts,
                playersPerCourt,
                pricePerHour
            );
            return ResponseEntity.ok(updatedRequest);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Invalid request format: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}

