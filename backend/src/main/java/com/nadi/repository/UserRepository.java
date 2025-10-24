package com.nadi.repository;

import com.nadi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * User data access
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // TODO: Add custom query methods
}

