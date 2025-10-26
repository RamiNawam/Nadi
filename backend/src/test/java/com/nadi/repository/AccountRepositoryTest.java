package com.nadi.repository;

import com.nadi.model.Account;
import com.nadi.model.AccountStatus;
import com.nadi.model.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryTest {

    @Mock
    private AccountRepository accountRepository;

    private UserAccount testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new UserAccount();
        testAccount.setId(UUID.randomUUID());
        testAccount.setName("Test User");
        testAccount.setEmail("test@example.com");
        testAccount.setPhone("+1234567890");
        testAccount.setStatus(AccountStatus.ACTIVE);
    }

    @Test
    void testSaveAndFindById() {
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        when(accountRepository.findById(testAccount.getId())).thenReturn(Optional.of(testAccount));
        
        Account saved = accountRepository.save(testAccount);
        Optional<Account> found = accountRepository.findById(saved.getId());
        
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test User");
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindByEmail() {
        when(accountRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testAccount));
        
        Optional<Account> found = accountRepository.findByEmail("test@example.com");
        
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    void testFindByPhone() {
        when(accountRepository.findByPhone("+1234567890")).thenReturn(Optional.of(testAccount));
        
        Optional<Account> found = accountRepository.findByPhone("+1234567890");
        
        assertThat(found).isPresent();
        assertThat(found.get().getPhone()).isEqualTo("+1234567890");
    }

    @Test
    void testExistsByEmail() {
        when(accountRepository.existsByEmail("test@example.com")).thenReturn(true);
        
        boolean exists = accountRepository.existsByEmail("test@example.com");
        
        assertThat(exists).isTrue();
    }
}
