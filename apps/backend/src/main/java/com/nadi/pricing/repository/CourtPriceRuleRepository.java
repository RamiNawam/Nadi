package com.nadi.pricing.repository;

import com.nadi.pricing.model.CourtPriceRule;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Court pricing rules data access
 */
@Repository
public interface CourtPriceRuleRepository extends MongoRepository<CourtPriceRule, String> {
    
    /**
     * Find price rules by court
     * @param courtId court ID
     * @return list of price rules for court
     */
    List<CourtPriceRule> findByCourtId(String courtId);
    
    /**
     * Find price rules by court and day of week
     * @param courtId court ID
     * @param dayOfWeek day of week (0-6)
     * @return list of price rules for court on specific day
     */
    List<CourtPriceRule> findByCourtIdAndDayOfWeek(String courtId, int dayOfWeek);
    
    /**
     * Find price rules by court, day, and time range
     * @param courtId court ID
     * @param dayOfWeek day of week
     * @param startTime start time
     * @param endTime end time
     * @return list of matching price rules
     */
    List<CourtPriceRule> findByCourtIdAndDayOfWeekAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
        String courtId, int dayOfWeek, String startTime, String endTime);
}
