package com.nadi.config;

import com.nadi.model.AccountStatus;
import com.nadi.model.DeveloperAccount;
import com.nadi.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Creates initial data on startup.
 * Only place that automatically creates database records.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
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

