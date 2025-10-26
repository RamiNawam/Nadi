package com.nadi.repository;

import com.nadi.model.Account;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends MongoRepository<Account, UUID> {
    Optional<Account> findByEmail(String email);
    Optional<Account> findByPhone(String phone);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);
}

