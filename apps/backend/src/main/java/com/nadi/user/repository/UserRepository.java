package com.nadi.user.repository;

import com.nadi.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * User data access
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    /**
     * Find user by email
     * @param email user email
     * @return user if found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find user by phone
     * @param phone user phone
     * @return user if found
     */
    Optional<User> findByPhone(String phone);
    
    /**
     * Check if email exists
     * @param email user email
     * @return true if exists
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if phone exists
     * @param phone user phone
     * @return true if exists
     */
    boolean existsByPhone(String phone);
}
