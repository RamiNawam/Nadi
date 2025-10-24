package com.nadi.reservation.repository;

import com.nadi.reservation.model.Reservation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.List;

/**
 * Reservation data access with overlap checking support
 */
@Repository
public interface ReservationRepository extends MongoRepository<Reservation, String> {
    
    /**
     * Find reservations by user
     * @param userId user ID
     * @return list of user's reservations
     */
    List<Reservation> findByUserId(String userId);
    
    /**
     * Find reservations by court
     * @param courtId court ID
     * @return list of reservations for court
     */
    List<Reservation> findByCourtId(String courtId);
    
    /**
     * Find reservations by status
     * @param status reservation status
     * @return list of reservations with status
     */
    List<Reservation> findByStatus(Reservation.ReservationStatus status);
    
    /**
     * Find expired holds for cleanup
     * @param now current time
     * @return list of expired holds
     */
    List<Reservation> findByHoldExpiresAtBefore(Instant now);
    
    /**
     * Find overlapping reservations for a court
     * Uses compound index for efficient overlap checking
     * @param courtId court ID
     * @param startTime reservation start time
     * @param endTime reservation end time
     * @return list of overlapping reservations
     */
    @Query("{'courtId': ?0, 'status': {$in: ['HELD', 'CONFIRMED']}, " +
           "'$or': [{'startTime': {$lt: ?2, $gte: ?1}}, " +
                   "{'endTime': {$gt: ?1, $lte: ?2}}, " +
                   "{'startTime': {$lte: ?1}, 'endTime': {$gte: ?2}}]}")
    List<Reservation> findOverlappingReservations(String courtId, Instant startTime, Instant endTime);
    
    /**
     * Find confirmed reservations by court and date range
     * @param courtId court ID
     * @param startTime start of date range
     * @param endTime end of date range
     * @return list of confirmed reservations
     */
    List<Reservation> findByCourtIdAndStatusAndStartTimeBetween(
        String courtId, Reservation.ReservationStatus status, Instant startTime, Instant endTime);
}
