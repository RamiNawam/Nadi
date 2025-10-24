package com.nadi.reservation.controller;

import com.nadi.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;

/**
 * Reservation endpoints for booking flow
 */
@RestController
@RequestMapping("/api/v1/reservations")
@CrossOrigin(origins = "http://localhost:3000")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    /**
     * Create a hold on a court
     */
    @PostMapping("/hold")
    public ResponseEntity<?> createHold(@RequestBody HoldRequest request) {
        // TODO: Validate request data
        // TODO: Create hold reservation
        // TODO: Return reservation with HELD status
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Confirm a held reservation
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> confirmReservation(@PathVariable String id, @RequestBody ConfirmRequest request) {
        // TODO: Confirm held reservation
        // TODO: Return confirmed reservation
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Cancel a reservation
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<?> cancelReservation(@PathVariable String id, @RequestBody CancelRequest request) {
        // TODO: Cancel reservation
        // TODO: Return cancelled reservation
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Get user's reservations
     */
    @GetMapping("/my")
    public ResponseEntity<?> getMyReservations(@RequestParam String userId) {
        // TODO: Return user's reservations
        throw new UnsupportedOperationException("Not implemented yet");
    }

    // DTOs
    public static class HoldRequest {
        public String courtId;
        public Instant startTime;
        public Instant endTime;
        public String userId;
        public int playersCount;
    }

    public static class ConfirmRequest {
        public String userId;
    }

    public static class CancelRequest {
        public String userId;
        public String reason;
    }
}
