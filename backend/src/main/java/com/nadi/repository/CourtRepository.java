package com.nadi.repository;

import com.nadi.model.Court;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface CourtRepository extends MongoRepository<Court, UUID> {
    List<Court> findBySport(com.nadi.model.SportType sport);
}

