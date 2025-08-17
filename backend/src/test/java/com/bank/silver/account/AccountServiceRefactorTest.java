package com.bank.silver.account;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.repository.AccountRepository;
import com.bank.silver.account.service.AccountService;
import com.bank.silver.account.util.AccountNumberGenerator;
import com.bank.silver.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceRefactorTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountNumberGenerator accountNumberGenerator;

    @InjectMocks
    private AccountService accountService;

    private User savedUser;
    @BeforeEach
    void setUp() {
        savedUser = new User("john", "password123", "john@example.com");
    }

    @Test
    void createAccount_shouldUseAccountNumberGenerato() {
        //given
        String generatedNumber = "123456789012";
        when(accountNumberGenerator.generate()).thenReturn(generatedNumber);
        when(accountRepository.existsByAccountNumber(generatedNumber)).thenReturn(false);

        Account fakeAccount = new Account(generatedNumber, BigDecimal.ZERO, savedUser);

        when(accountRepository.save(any(Account.class))).thenReturn(fakeAccount);

        //when
        Account account = accountService.createAccount(savedUser);
        //then
        assertThat(account.getAccountNumber()).isEqualTo(generatedNumber);
        assertThat(account.getBalance()).isEqualTo(0L);
        assertThat(account.getOwner()).isEqualTo(savedUser);
        verify(accountNumberGenerator, times(1)).generate();
    }

    @Test
    void generate_shoudReturn12DigitNumber() {
        AccountNumberGenerator generator = new AccountNumberGenerator();

        String accountNumber = generator.generate();

        assertThat(accountNumber)
                .matches("\\d{12}")
                .hasSize(12);
    }
}
