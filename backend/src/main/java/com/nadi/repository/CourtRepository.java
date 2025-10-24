package com.nadi.repository;

import com.nadi.model.Court;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Court data access
 */
@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {
    // TODO: Add custom query methods for court availability
}

