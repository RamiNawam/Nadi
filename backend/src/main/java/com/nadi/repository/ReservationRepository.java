package com.nadi.repository;

import com.nadi.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends MongoRepository<Reservation, UUID> {
    List<Reservation> findByUserAccountId(UUID userAccountId);
    List<Reservation> findByCourtId(UUID courtId);
    List<Reservation> findByStartTimeBetween(OffsetDateTime start, OffsetDateTime end);
    List<Reservation> findByStatus(String status);
}

