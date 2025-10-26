package com.nadi.repository;

import com.nadi.model.VenueAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface VenueAccountRepository extends MongoRepository<VenueAccount, UUID> {
    List<VenueAccount> findByCompanyNameContainingIgnoreCase(String companyName);
}

