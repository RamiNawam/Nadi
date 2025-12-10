package com.nadi.controller;

import com.nadi.dto.account.AccountRequestDto;
import com.nadi.dto.account.AccountResponseDto;
import com.nadi.model.Account;
import com.nadi.model.AccountStatus;
import com.nadi.model.DeveloperAccount;
import com.nadi.model.UserAccount;
import com.nadi.model.VenueAccount;
import com.nadi.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AccountRequestDto requestDto;
    private UserAccount testAccount;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        
        requestDto = new AccountRequestDto();
        requestDto.setName("John Doe");
        requestDto.setEmail("john@example.com");
        requestDto.setPhone("+1234567890");

        testAccount = new UserAccount();
        testAccount.setId(testId);
        testAccount.setName("John Doe");
        testAccount.setEmail("john@example.com");
        testAccount.setPhone("+1234567890");
        testAccount.setStatus(AccountStatus.ACTIVE);
    }

    @Test
    void testCreateUserAccount_Success() {
        when(accountService.createUserAccount(anyString(), anyString(), anyString()))
            .thenReturn(testAccount);

        ResponseEntity<AccountResponseDto> response = accountController.createUserAccount(requestDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("John Doe");
        verify(accountService).createUserAccount("John Doe", "john@example.com", "+1234567890");
    }

    @Test
    void testCreateDeveloperAccount_Success() {
        DeveloperAccount devAccount = new DeveloperAccount();
        devAccount.setId(testId);
        devAccount.setName("Dev User");
        devAccount.setEmail("dev@example.com");
        devAccount.setPhone("+1234567890");
        
        when(accountService.createDeveloperAccount(anyString(), anyString(), anyString()))
            .thenReturn(devAccount);

        ResponseEntity<AccountResponseDto> response = accountController.createDeveloperAccount(requestDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        verify(accountService).createDeveloperAccount(anyString(), anyString(), anyString());
    }

    @Test
    void testCreateVenueAccount_Success() {
        VenueAccount venueAccount = new VenueAccount();
        venueAccount.setId(testId);
        venueAccount.setName("Venue Owner");
        venueAccount.setEmail("venue@example.com");
        venueAccount.setPhone("+1234567890");
        venueAccount.setCompanyName("Test Company");
        
        when(accountService.createVenueAccount(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(venueAccount);

        ResponseEntity<AccountResponseDto> response = accountController.createVenueAccount(requestDto);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        verify(accountService).createVenueAccount(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testGetAccount_Exists() {
        when(accountService.findById(testId)).thenReturn(Optional.of(testAccount));

        ResponseEntity<AccountResponseDto> response = accountController.getAccount(testId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testGetAccount_NotFound() {
        when(accountService.findById(testId)).thenReturn(Optional.empty());

        ResponseEntity<AccountResponseDto> response = accountController.getAccount(testId);

        assertThat(response.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    void testDeactivateAccount_Success() {
        doNothing().when(accountService).deactivateAccount(testId);

        ResponseEntity<Void> response = accountController.deactivateAccount(testId);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        verify(accountService).deactivateAccount(testId);
    }

    @Test
    void testDeleteAccount_Success() {
        doNothing().when(accountService).deactivateAccount(testId);

        ResponseEntity<Void> response = accountController.deleteAccount(testId);

        assertThat(response.getStatusCodeValue()).isEqualTo(204);
        verify(accountService).deactivateAccount(testId);
    }

    @Test
    void testGetAllAccounts_Success() {
        List<Account> accounts = Arrays.asList(testAccount);
        when(accountService.getAllAccounts()).thenReturn(accounts);

        ResponseEntity<List<AccountResponseDto>> response = accountController.getAllAccounts();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(1);
    }
}

