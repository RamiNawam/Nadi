package com.nadi.repository;

import com.nadi.model.Venue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Venue data access
 */
@Repository
public interface VenueRepository extends JpaRepository<Venue, Long> {
    // TODO: Add custom query methods for location-based search
}

