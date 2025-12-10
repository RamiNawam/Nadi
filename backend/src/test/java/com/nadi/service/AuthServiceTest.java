package com.nadi.service;

import com.nadi.dto.auth.LoginRequestDto;
import com.nadi.dto.auth.LoginResponseDto;
import com.nadi.dto.auth.RegisterRequestDto;
import com.nadi.model.Account;
import com.nadi.model.AccountStatus;
import com.nadi.model.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AuthService authService;

    private UserAccount testAccount;
    private UUID testId;
    private LoginRequestDto loginRequest;
    private RegisterRequestDto registerRequest;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        
        testAccount = new UserAccount();
        testAccount.setId(testId);
        testAccount.setName("John Doe");
        testAccount.setEmail("john@example.com");
        testAccount.setPhone("+1234567890");
        testAccount.setPassword("password123");
        testAccount.setStatus(AccountStatus.ACTIVE);

        loginRequest = new LoginRequestDto();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("password123");

        registerRequest = new RegisterRequestDto();
        registerRequest.setName("Jane Doe");
        registerRequest.setEmail("jane@example.com");
        registerRequest.setPhone("+1234567890");
        registerRequest.setPassword("password123");
        registerRequest.setAccountType("user");
    }

    @Test
    void testLogin_Success() {
        when(accountService.findByEmail("john@example.com")).thenReturn(Optional.of(testAccount));

        LoginResponseDto response = authService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getUserId()).isEqualTo(testId);
        assertThat(response.getEmail()).isEqualTo("john@example.com");
        assertThat(response.getName()).isEqualTo("John Doe");
        assertThat(response.getAccountType()).isEqualTo("user");
        assertThat(response.getToken()).isNotNull();
    }

    @Test
    void testLogin_InvalidEmail() {
        when(accountService.findByEmail("john@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(loginRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Invalid email or password");
    }

    @Test
    void testLogin_InvalidPassword() {
        UserAccount accountWithWrongPassword = new UserAccount();
        accountWithWrongPassword.setId(testId);
        accountWithWrongPassword.setEmail("john@example.com");
        accountWithWrongPassword.setPassword("wrongpassword");
        
        when(accountService.findByEmail("john@example.com")).thenReturn(Optional.of(accountWithWrongPassword));

        assertThatThrownBy(() -> authService.login(loginRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Invalid email or password");
    }

    @Test
    void testRegister_UserAccount() {
        UserAccount newAccount = new UserAccount();
        newAccount.setId(testId);
        newAccount.setName("Jane Doe");
        newAccount.setEmail("jane@example.com");
        newAccount.setPhone("+1234567890");
        
        when(accountService.findByEmail("jane@example.com")).thenReturn(Optional.empty());
        when(accountService.createUserAccount(anyString(), anyString(), anyString()))
            .thenReturn(newAccount);
        when(accountService.save(any(UserAccount.class))).thenReturn(newAccount);

        LoginResponseDto response = authService.register(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("jane@example.com");
        assertThat(response.getAccountType()).isEqualTo("user");
        assertThat(response.getToken()).isNotNull();
    }

    @Test
    void testRegister_EmailAlreadyExists() {
        when(accountService.findByEmail("jane@example.com")).thenReturn(Optional.of(testAccount));

        assertThatThrownBy(() -> authService.register(registerRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Email already registered");
    }

    @Test
    void testRegister_InvalidAccountType() {
        registerRequest.setAccountType("invalid");
        when(accountService.findByEmail("jane@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.register(registerRequest))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Invalid account type");
    }

    @Test
    void testRegister_VenueAccount() {
        registerRequest.setAccountType("venue");
        registerRequest.setCompanyName("Test Company");
        
        com.nadi.model.VenueAccount venueAccount = new com.nadi.model.VenueAccount();
        venueAccount.setId(testId);
        venueAccount.setName("Venue Owner");
        venueAccount.setEmail("venue@example.com");
        venueAccount.setPhone("+1234567890");
        venueAccount.setCompanyName("Test Company");
        
        when(accountService.findByEmail("jane@example.com")).thenReturn(Optional.empty());
        when(accountService.createVenueAccount(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(venueAccount);
        when(accountService.save(any(com.nadi.model.VenueAccount.class))).thenReturn(venueAccount);

        LoginResponseDto response = authService.register(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getAccountType()).isEqualTo("venue");
        assertThat(response.getToken()).isNotNull();
    }

    @Test
    void testRegister_DeveloperAccount() {
        registerRequest.setAccountType("developer");
        
        com.nadi.model.DeveloperAccount devAccount = new com.nadi.model.DeveloperAccount();
        devAccount.setId(testId);
        devAccount.setName("Developer");
        devAccount.setEmail("dev@example.com");
        devAccount.setPhone("+1234567890");
        
        when(accountService.findByEmail("jane@example.com")).thenReturn(Optional.empty());
        when(accountService.createDeveloperAccount(anyString(), anyString(), anyString()))
            .thenReturn(devAccount);
        when(accountService.save(any(com.nadi.model.DeveloperAccount.class))).thenReturn(devAccount);

        LoginResponseDto response = authService.register(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getAccountType()).isEqualTo("developer");
        assertThat(response.getToken()).isNotNull();
    }
}

