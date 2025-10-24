package com.nadi.sport.repository;

import com.nadi.sport.model.Sport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * Sport data access
 */
@Repository
public interface SportRepository extends MongoRepository<Sport, String> {
    
    /**
     * Find sport by name
     * @param name sport type
     * @return sport if found
     */
    Optional<Sport> findByName(Sport.SportType name);
    
    /**
     * Check if sport exists by name
     * @param name sport type
     * @return true if exists
     */
    boolean existsByName(Sport.SportType name);
}
