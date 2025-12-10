package com.nadi.repository;

import com.nadi.model.Venue;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface VenueRepository extends MongoRepository<Venue, String> {
    List<Venue> findByNameContainingIgnoreCase(String name);
    List<Venue> findByAddressContainingIgnoreCase(String address);
}

