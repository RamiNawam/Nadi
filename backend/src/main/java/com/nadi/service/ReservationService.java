package com.nadi.service;

import com.nadi.model.Money;
import com.nadi.model.Reservation;
import com.nadi.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation createReservation(UUID userAccountId, UUID courtId, OffsetDateTime startTime, 
                                         OffsetDateTime endTime, Money totalPrice, String contactPhone) {
        String code = "RES-" + System.currentTimeMillis();
        
        Reservation reservation = new Reservation();
        reservation.setId(UUID.randomUUID());
        reservation.setCode(code);
        reservation.setUserAccountId(userAccountId);
        reservation.setCourtId(courtId);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setTotalPrice(totalPrice);
        reservation.setContactPhone(contactPhone);
        reservation.setStatus("PENDING");

        return reservationRepository.save(reservation);
    }

    public Optional<Reservation> findById(UUID id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> findByUserId(UUID userId) {
        return reservationRepository.findByUserAccountId(userId);
    }

    public List<Reservation> findByCourtId(UUID courtId) {
        return reservationRepository.findByCourtId(courtId);
    }

    public List<Reservation> findByStatus(String status) {
        return reservationRepository.findByStatus(status);
    }

    public List<Reservation> findByTimeRange(OffsetDateTime start, OffsetDateTime end) {
        return reservationRepository.findByStartTimeBetween(start, end);
    }

    public void confirmReservation(UUID id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()) {
            throw new RuntimeException("Reservation not found");
        }
        Reservation res = reservation.get();
        res.setStatus("CONFIRMED");
        reservationRepository.save(res);
    }

    public void cancelReservation(UUID id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()) {
            throw new RuntimeException("Reservation not found");
        }
        Reservation res = reservation.get();
        res.cancel();
        reservationRepository.save(res);
    }

    public void updateReservationTime(UUID id, OffsetDateTime newStart, OffsetDateTime newEnd) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isEmpty()) {
            throw new RuntimeException("Reservation not found");
        }
        Reservation res = reservation.get();
        res.reschedule(newStart, newEnd);
        reservationRepository.save(res);
    }

    public void deleteReservation(UUID id) {
        if (!reservationRepository.existsById(id)) {
            throw new RuntimeException("Reservation not found");
        }
        reservationRepository.deleteById(id);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
}

