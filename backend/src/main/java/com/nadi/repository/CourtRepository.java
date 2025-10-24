package com.nadi.repository;

import com.nadi.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data access layer for Court entities.
 * 
 * This repository will handle all database operations related to courts:
 * - CRUD operations
 * - Court availability queries
 * - Sport type filtering
 */
@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
    // TODO: Add custom query methods for court availability
}

