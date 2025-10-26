package com.nadi.service;

import com.nadi.model.Account;
import com.nadi.model.AccountStatus;
import com.nadi.model.UserAccount;
import com.nadi.model.VenueAccount;
import com.nadi.model.DeveloperAccount;
import com.nadi.repository.AccountRepository;
import com.nadi.repository.UserAccountRepository;
import com.nadi.repository.VenueAccountRepository;
import com.nadi.repository.DeveloperAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private VenueAccountRepository venueAccountRepository;

    @Autowired
    private DeveloperAccountRepository developerAccountRepository;

    public UserAccount createUserAccount(String name, String email, String phone) {
        if (accountRepository.existsByEmail(email)) {
            throw new RuntimeException("Account with this email already exists");
        }
        if (accountRepository.existsByPhone(phone)) {
            throw new RuntimeException("Account with this phone already exists");
        }

        UserAccount account = new UserAccount();
        account.setId(UUID.randomUUID());
        account.setName(name);
        account.setEmail(email);
        account.setPhone(phone);
        account.setStatus(AccountStatus.ACTIVE);

        return userAccountRepository.save(account);
    }

    public VenueAccount createVenueAccount(String name, String email, String phone, String companyName) {
        if (accountRepository.existsByEmail(email)) {
            throw new RuntimeException("Account with this email already exists");
        }
        if (accountRepository.existsByPhone(phone)) {
            throw new RuntimeException("Account with this phone already exists");
        }

        VenueAccount account = new VenueAccount();
        account.setId(UUID.randomUUID());
        account.setName(name);
        account.setEmail(email);
        account.setPhone(phone);
        account.setCompanyName(companyName);
        account.setStatus(AccountStatus.ACTIVE);

        return venueAccountRepository.save(account);
    }

    public DeveloperAccount createDeveloperAccount(String name, String email, String phone) {
        if (accountRepository.existsByEmail(email)) {
            throw new RuntimeException("Account with this email already exists");
        }
        if (accountRepository.existsByPhone(phone)) {
            throw new RuntimeException("Account with this phone already exists");
        }

        DeveloperAccount account = new DeveloperAccount();
        account.setId(UUID.randomUUID());
        account.setName(name);
        account.setEmail(email);
        account.setPhone(phone);
        account.setStatus(AccountStatus.ACTIVE);

        return developerAccountRepository.save(account);
    }

    public Optional<Account> findById(UUID id) {
        return accountRepository.findById(id);
    }

    public Optional<Account> findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

    public Optional<Account> findByPhone(String phone) {
        return accountRepository.findByPhone(phone);
    }

    public void deactivateAccount(UUID id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
            Account acc = account.get();
            acc.deactivate();
            accountRepository.save(acc);
        } else {
            throw new RuntimeException("Account not found");
        }
    }

    public boolean authenticate(String email, String password) {
        Optional<Account> account = accountRepository.findByEmail(email);
        return account.isPresent() && account.get().authenticate();
    }
}

