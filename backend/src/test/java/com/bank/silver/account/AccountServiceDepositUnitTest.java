package com.bank.silver.account;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.entity.Transaction;
import com.bank.silver.account.entity.TransactionType;
import com.bank.silver.account.repository.AccountRepository;
import com.bank.silver.account.repository.TransactionRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceDepositUnitTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = new User("john", "password123", "john@example.com");
    }

    @Test
    void deposit_shouldCreateTransactionHistory() {
        //given
        Account account = new Account("123456789012", BigDecimal.ZERO, savedUser);
        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(inv -> inv.getArgument(0, Account.class));

        //when
        accountService.deposit("123456789012", BigDecimal.valueOf(1000));

        //then
        assertThat(account.getTransactions()).hasSize(1);
        Transaction tx = account.getTransactions().get(0);
        assertThat(tx.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(tx.getAmount()).isEqualByComparingTo("1000");

    }
}
