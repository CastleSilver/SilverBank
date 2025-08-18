package com.bank.silver.account;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.repository.AccountRepository;
import com.bank.silver.account.service.AccountService;
import com.bank.silver.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceWithdrawTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private User savedUser;

    private Account account;

    @BeforeEach
    void setUp() {
        savedUser = new User("john", "password123", "john@example.com");
        account = new Account("123456789012", BigDecimal.valueOf(1000), savedUser);
    }

    @Test
    void withdraw_success_shouldReduceBalance() {
        when(accountRepository.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);

        Account result = accountService.withdraw("123456789012", BigDecimal.valueOf(200));

        assertThat(result.getBalance()).isEqualByComparingTo("800");
    }

    @Test
    void withdraw_fail_whenInsufficientBalance() {
        when(accountRepository.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(account));

        assertThatThrownBy(() ->
                accountService.withdraw("123456789012", BigDecimal.valueOf(2000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient balance");
    }

    @Test
    void withdraw_fail_whenNegativeOrZeroAmount() {
        when(accountRepository.findByAccountNumber(anyString()))
                .thenReturn(Optional.of(account));

        assertThatThrownBy(() ->
                accountService.withdraw("123456789012", BigDecimal.ZERO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Withdraw amount must be positive");
    }
}
