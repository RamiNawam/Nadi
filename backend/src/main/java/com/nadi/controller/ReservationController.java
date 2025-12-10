package com.nadi.controller;

import com.nadi.dto.reservation.ReservationRequestDto;
import com.nadi.dto.reservation.ReservationResponseDto;
import com.nadi.model.Reservation;
import com.nadi.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/reservations")
@CrossOrigin(origins = "*")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto request) {
        Reservation reservation = reservationService.createReservation(
            request.getUserAccountId(),
            request.getCourtId(),
            request.getStartTime(),
            request.getEndTime(),
            request.getTotalPrice(),
            request.getContactPhone()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponseDto(reservation));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservation(@PathVariable String id) {
        return reservationService.findById(java.util.UUID.fromString(id))
            .map(reservation -> ResponseEntity.ok(mapToResponseDto(reservation)))
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        List<ReservationResponseDto> reservations = reservationService.getAllReservations().stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByUser(@PathVariable String userId) {
        List<ReservationResponseDto> reservations = reservationService.findByUserId(java.util.UUID.fromString(userId))
            .stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/court/{courtId}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByCourt(@PathVariable String courtId) {
        List<ReservationResponseDto> reservations = reservationService.findByCourtId(java.util.UUID.fromString(courtId))
            .stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReservationResponseDto>> getReservationsByStatus(@PathVariable String status) {
        List<ReservationResponseDto> reservations = reservationService.findByStatus(status)
            .stream()
            .map(this::mapToResponseDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(reservations);
    }

    @PutMapping("/{id}/confirm")
    public ResponseEntity<Void> confirmReservation(@PathVariable String id) {
        reservationService.confirmReservation(java.util.UUID.fromString(id));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(@PathVariable String id) {
        try {
            reservationService.cancelReservation(java.util.UUID.fromString(id));
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/reschedule")
    public ResponseEntity<ReservationResponseDto> rescheduleReservation(
        @PathVariable String id,
        @RequestBody ReservationRequestDto request
    ) {
        try {
            reservationService.updateReservationTime(
                java.util.UUID.fromString(id),
                request.getStartTime(),
                request.getEndTime()
            );
            return reservationService.findById(java.util.UUID.fromString(id))
                .map(reservation -> ResponseEntity.ok(mapToResponseDto(reservation)))
                .orElse(ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable String id) {
        try {
            reservationService.deleteReservation(java.util.UUID.fromString(id));
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private ReservationResponseDto mapToResponseDto(Reservation reservation) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setId(reservation.getId());
        dto.setCode(reservation.getCode());
        dto.setUserAccountId(reservation.getUserAccountId());
        dto.setCourtId(reservation.getCourtId());
        dto.setStartTime(reservation.getStartTime());
        dto.setEndTime(reservation.getEndTime());
        dto.setStatus(reservation.getStatus());
        dto.setTotalPrice(reservation.getTotalPrice());
        dto.setContactPhone(reservation.getContactPhone());
        return dto;
    }
}

