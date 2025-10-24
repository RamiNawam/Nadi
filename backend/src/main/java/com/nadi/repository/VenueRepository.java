package com.nadi.repository;

import com.nadi.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data access layer for Venue entities.
 * 
 * This repository will handle all database operations related to venues:
 * - CRUD operations
 * - Location-based searches
 * - Venue filtering by sport type
 */
@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    // TODO: Add custom query methods for location-based search
}

