package com.nadi.repository;

import com.nadi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Data access layer for User entities.
 * 
 * This repository will handle all database operations related to users:
 * - CRUD operations
 * - Custom queries for user authentication
 * - Search functionality
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // TODO: Add custom query methods
}

