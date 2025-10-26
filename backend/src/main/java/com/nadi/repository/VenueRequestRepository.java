package com.nadi.repository;

import com.nadi.model.VenueRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface VenueRequestRepository extends MongoRepository<VenueRequest, UUID> {
    List<VenueRequest> findByStatus(String status);
}

