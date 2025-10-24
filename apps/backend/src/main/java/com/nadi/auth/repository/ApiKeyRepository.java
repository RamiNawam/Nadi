package com.nadi.auth.repository;

import com.nadi.auth.model.ApiKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * API key data access
 */
@Repository
public interface ApiKeyRepository extends MongoRepository<ApiKey, String> {
    
    /**
     * Find API key by key value
     * @param key API key string
     * @return API key if found
     */
    Optional<ApiKey> findByKey(String key);
    
    /**
     * Find API keys by user
     * @param userId user ID
     * @return list of user's API keys
     */
    List<ApiKey> findByUserId(String userId);
    
    /**
     * Check if API key exists
     * @param key API key string
     * @return true if exists
     */
    boolean existsByKey(String key);
    
    /**
     * Find API keys by user and key
     * @param userId user ID
     * @param key API key string
     * @return API key if found
     */
    Optional<ApiKey> findByUserIdAndKey(String userId, String key);
}
