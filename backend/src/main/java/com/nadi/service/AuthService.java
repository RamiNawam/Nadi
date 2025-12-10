package com.nadi.service;

import com.nadi.dto.auth.LoginRequestDto;
import com.nadi.dto.auth.LoginResponseDto;
import com.nadi.dto.auth.RegisterRequestDto;
import com.nadi.model.Account;
import com.nadi.model.DeveloperAccount;
import com.nadi.model.UserAccount;
import com.nadi.model.VenueAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private AccountService accountService;

    public LoginResponseDto login(LoginRequestDto request) {
        Optional<Account> account = accountService.findByEmail(request.getEmail());
        
        if (account.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }
        
        Account acc = account.get();
        
        // Check password
        if (!acc.authenticate(request.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        
        LoginResponseDto response = new LoginResponseDto();
        response.setToken(generateSimpleToken(acc.getId()));
        response.setUserId(acc.getId());
        response.setName(acc.getName());
        response.setEmail(acc.getEmail());
        response.setAccountType(getAccountType(acc));
        
        return response;
    }

    public LoginResponseDto register(RegisterRequestDto request) {
        // Check if email already exists
        if (accountService.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }
        
        Account createdAccount = switch (request.getAccountType()) {
            case "user" -> {
                UserAccount userAccount = accountService.createUserAccount(
                    request.getName(),
                    request.getEmail(),
                    request.getPhone()
                );
                userAccount.setPassword(request.getPassword());
                yield accountService.save(userAccount);
            }
            case "venue" -> {
                VenueAccount venueAccount = accountService.createVenueAccount(
                    request.getName(),
                    request.getEmail(),
                    request.getPhone(),
                    request.getCompanyName() != null ? request.getCompanyName() : "Company"
                );
                venueAccount.setPassword(request.getPassword());
                yield accountService.save(venueAccount);
            }
            case "developer" -> {
                DeveloperAccount devAccount = accountService.createDeveloperAccount(
                    request.getName(),
                    request.getEmail(),
                    request.getPhone()
                );
                devAccount.setPassword(request.getPassword());
                yield accountService.save(devAccount);
            }
            default -> throw new RuntimeException("Invalid account type");
        };
        
        LoginResponseDto response = new LoginResponseDto();
        response.setToken(generateSimpleToken(createdAccount.getId()));
        response.setUserId(createdAccount.getId());
        response.setName(createdAccount.getName());
        response.setEmail(createdAccount.getEmail());
        response.setAccountType(getAccountType(createdAccount));
        
        return response;
    }

    private String generateSimpleToken(UUID userId) {
        // Simple token generation (in production, use JWT)
        return "token_" + userId.toString() + "_" + System.currentTimeMillis();
    }

    private String getAccountType(Account account) {
        if (account instanceof UserAccount) return "user";
        if (account instanceof VenueAccount) return "venue";
        if (account instanceof DeveloperAccount) return "developer";
        return "unknown";
    }
}

