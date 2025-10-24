package com.nadi.venue.repository;

import com.nadi.venue.model.Venue;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Venue data access with geospatial search support
 */
@Repository
public interface VenueRepository extends MongoRepository<Venue, String> {
    
    /**
     * Find venues near a location using MongoDB 2dsphere index
     * @param location center point
     * @param distance search radius
     * @return list of nearby venues
     */
    List<Venue> findByLocationNear(Point location, Distance distance);
    
    /**
     * Find venues by owner
     * @param ownerId venue owner ID
     * @return list of venues owned by user
     */
    List<Venue> findByOwnerId(String ownerId);
    
    /**
     * Find venues by city
     * @param city city name
     * @return list of venues in city
     */
    List<Venue> findByCity(String city);
    
    /**
     * Find venues by name containing text
     * @param name venue name pattern
     * @return list of matching venues
     */
    List<Venue> findByNameContainingIgnoreCase(String name);
    
    /**
     * Find venues with geospatial query and additional filters
     * @param location center point
     * @param distance search radius
     * @param city optional city filter
     * @return list of matching venues
     */
    @Query("{'location': {$near: {$geometry: ?0, $maxDistance: ?1}}, 'city': ?2}")
    List<Venue> findNearbyVenuesInCity(Point location, double distance, String city);
}
