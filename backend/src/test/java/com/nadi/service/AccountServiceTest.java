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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for AccountService.
 * Uses mocked repositories - no database interaction.
 */
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private VenueAccountRepository venueAccountRepository;

    @Mock
    private DeveloperAccountRepository developerAccountRepository;

    @InjectMocks
    private AccountService accountService;

    private String testEmail;
    private String testPhone;
    private String testName;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
        testPhone = "+1234567890";
        testName = "Test User";
    }

    @Test
    void testCreateUserAccount_Success() {
        when(accountRepository.existsByEmail(testEmail)).thenReturn(false);
        when(accountRepository.existsByPhone(testPhone)).thenReturn(false);
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserAccount result = accountService.createUserAccount(testName, testEmail, testPhone);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(testEmail);
        assertThat(result.getPhone()).isEqualTo(testPhone);
        assertThat(result.getStatus()).isEqualTo(AccountStatus.ACTIVE);
    }

    @Test
    void testCreateUserAccount_DuplicateEmail() {
        when(accountRepository.existsByEmail(testEmail)).thenReturn(true);

        assertThatThrownBy(() -> accountService.createUserAccount(testName, testEmail, testPhone))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("email already exists");
    }

    @Test
    void testCreateVenueAccount_Success() {
        when(accountRepository.existsByEmail(testEmail)).thenReturn(false);
        when(accountRepository.existsByPhone(testPhone)).thenReturn(false);
        when(venueAccountRepository.save(any(VenueAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));

        VenueAccount result = accountService.createVenueAccount(testName, testEmail, testPhone, "Test Company");

        assertThat(result).isNotNull();
        assertThat(result.getCompanyName()).isEqualTo("Test Company");
    }

    @Test
    void testFindById() {
        UserAccount account = new UserAccount();
        account.setId(UUID.randomUUID());
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        Optional<Account> result = accountService.findById(account.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(account.getId());
    }

    @Test
    void testDeactivateAccount() {
        UserAccount account = new UserAccount();
        account.setId(UUID.randomUUID());
        when(accountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(accountRepository.save(any())).thenReturn(account);

        accountService.deactivateAccount(account.getId());

        verify(accountRepository).save(any());
    }

    @Test
    void testDeactivateAccount_NotFound() {
        when(accountRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.deactivateAccount(UUID.randomUUID()))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Account not found");
    }

    @Test
    void testAuthenticate_Success() {
        UserAccount account = new UserAccount();
        account.setId(UUID.randomUUID());
        account.setPassword("password");
        when(accountRepository.findByEmail(testEmail)).thenReturn(Optional.of(account));

        boolean result = accountService.authenticate(testEmail, "password");

        assertThat(result).isTrue();
    }

    @Test
    void testAuthenticate_WrongPassword() {
        UserAccount account = new UserAccount();
        account.setId(UUID.randomUUID());
        account.setPassword("correctpassword");
        when(accountRepository.findByEmail(testEmail)).thenReturn(Optional.of(account));

        boolean result = accountService.authenticate(testEmail, "wrongpassword");

        assertThat(result).isFalse();
    }

    @Test
    void testAuthenticate_AccountNotFound() {
        when(accountRepository.findByEmail(testEmail)).thenReturn(Optional.empty());

        boolean result = accountService.authenticate(testEmail, "password");

        assertThat(result).isFalse();
    }
}

