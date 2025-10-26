package com.nadi.repository;

import com.nadi.model.UserAccount;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface UserAccountRepository extends MongoRepository<UserAccount, UUID> {
}

