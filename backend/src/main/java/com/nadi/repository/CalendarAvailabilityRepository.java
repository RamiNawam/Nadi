package com.nadi.repository;

import com.nadi.model.CalendarAvailability;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface CalendarAvailabilityRepository extends MongoRepository<CalendarAvailability, UUID> {
}

