package com.nadi.controller;

import com.nadi.dto.account.AccountRequestDto;
import com.nadi.dto.account.AccountResponseDto;
import com.nadi.dto.venue.VenueResponseDto;
import com.nadi.model.Account;
import com.nadi.model.DeveloperAccount;
import com.nadi.model.UserAccount;
import com.nadi.model.VenueAccount;
import com.nadi.service.AccountService;
import com.nadi.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/v1/accounts")
@CrossOrigin(origins = "*")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private VenueService venueService;

    @PostMapping("/users")
    public ResponseEntity<AccountResponseDto> createUserAccount(@RequestBody AccountRequestDto request) {
        UserAccount account = accountService.createUserAccount(
            request.getName(),
            request.getEmail(),
            request.getPhone()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponseDto(account, "user"));
    }

    @PostMapping("/venues")
    public ResponseEntity<AccountResponseDto> createVenueAccount(@RequestBody AccountRequestDto request) {
        VenueAccount account = accountService.createVenueAccount(
            request.getName(),
            request.getEmail(),
            request.getPhone(),
            "Company Name"
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponseDto(account, "venue"));
    }

    @PostMapping("/developers")
    public ResponseEntity<AccountResponseDto> createDeveloperAccount(@RequestBody AccountRequestDto request) {
        DeveloperAccount account = accountService.createDeveloperAccount(
            request.getName(),
            request.getEmail(),
            request.getPhone()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponseDto(account, "developer"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccount(@PathVariable UUID id) {
        Optional<Account> account = accountService.findById(id);
        if (account.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(mapToResponseDto(account.get(), getAccountType(account.get())));
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts() {
        List<AccountResponseDto> accounts = accountService.getAllAccounts().stream()
            .map(account -> mapToResponseDto(account, getAccountType(account)))
            .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(accounts);
    }

    @PutMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateAccount(@PathVariable UUID id) {
        accountService.deactivateAccount(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable UUID id) {
        try {
            accountService.deleteAccount(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            java.util.Map<String, String> error = new java.util.HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    private AccountResponseDto mapToResponseDto(Account account, String type) {
        AccountResponseDto dto = new AccountResponseDto();
        dto.setId(account.getId());
        dto.setName(account.getName());
        dto.setEmail(account.getEmail());
        dto.setPhone(account.getPhone());
        dto.setStatus(account.getStatus().toString());
        dto.setType(type);
        
        // For venue accounts, include venue information if available
        if (account instanceof VenueAccount) {
            VenueAccount venueAccount = (VenueAccount) account;
            if (venueAccount.getVenue() != null) {
                VenueResponseDto venueDto = new VenueResponseDto();
                venueDto.setId(venueAccount.getVenue().getId()); // Venue ID is String
                venueDto.setName(venueAccount.getVenue().getName());
                venueDto.setAddress(venueAccount.getVenue().getAddress());
                venueDto.setCafeteriaAvailable(venueAccount.getVenue().isCafeteriaAvailable());
                if (venueAccount.getVenue().getLocation() != null) {
                    venueDto.setLocation(venueAccount.getVenue().getLocation());
                }
                dto.setVenue(venueDto);
            }
        }
        
        return dto;
    }

    private String getAccountType(Account account) {
        if (account instanceof UserAccount) return "user";
        if (account instanceof VenueAccount) return "venue";
        if (account instanceof DeveloperAccount) return "developer";
        return "unknown";
    }
}

