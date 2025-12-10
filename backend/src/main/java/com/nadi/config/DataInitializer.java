package com.nadi.config;

import com.nadi.model.AccountStatus;
import com.nadi.model.DeveloperAccount;
import com.nadi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * DataInitializer creates initial data on application startup.
 * Currently creates a developer account for testing/admin purposes.
 * 
 * NOTE: This is the ONLY place that automatically creates data in the database.
 * All unit tests use mocked repositories and do NOT create real database records.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create developer account if it doesn't exist
        String devEmail = "RamiNawam@nadi.dev";
        if (accountRepository.findByEmail(devEmail).isEmpty()) {
            DeveloperAccount devAccount = new DeveloperAccount();
            devAccount.setId(UUID.randomUUID());
            devAccount.setName("Rami Nawam");
            devAccount.setEmail(devEmail);
            devAccount.setPhone("+96100000000");
            devAccount.setPassword("Rami2004");
            devAccount.setStatus(AccountStatus.ACTIVE);
            
            accountRepository.save(devAccount);
            System.out.println("✅ Developer account created: " + devEmail);
        } else {
            System.out.println("ℹ️  Developer account already exists: " + devEmail);
        }
    }
}

