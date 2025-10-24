package com.nadi.court.repository;

import com.nadi.court.model.Court;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Court data access
 */
@Repository
public interface CourtRepository extends MongoRepository<Court, String> {
    
    /**
     * Find courts by venue
     * @param venueId venue ID
     * @return list of courts in venue
     */
    List<Court> findByVenueId(String venueId);
    
    /**
     * Find courts by sport
     * @param sportId sport ID
     * @return list of courts for sport
     */
    List<Court> findBySportId(String sportId);
    
    /**
     * Find courts by venue and sport
     * @param venueId venue ID
     * @param sportId sport ID
     * @return list of matching courts
     */
    List<Court> findByVenueIdAndSportId(String venueId, String sportId);
    
    /**
     * Find active courts by venue
     * @param venueId venue ID
     * @return list of active courts
     */
    List<Court> findByVenueIdAndIsActiveTrue(String venueId);
    
    /**
     * Find active courts by sport
     * @param sportId sport ID
     * @return list of active courts
     */
    List<Court> findBySportIdAndIsActiveTrue(String sportId);
}
