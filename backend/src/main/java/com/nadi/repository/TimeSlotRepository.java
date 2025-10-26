package com.nadi.repository;

import com.nadi.model.TimeSlot;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface TimeSlotRepository extends MongoRepository<TimeSlot, UUID> {
    List<TimeSlot> findByStatus(String status);
    List<TimeSlot> findByStartTimeBetween(OffsetDateTime start, OffsetDateTime end);
}

