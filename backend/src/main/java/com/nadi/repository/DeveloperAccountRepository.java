package com.nadi.repository;

import com.nadi.model.DeveloperAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface DeveloperAccountRepository extends MongoRepository<DeveloperAccount, UUID> {
}

