package com.bank.silver.account;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.entity.TransactionType;
import com.bank.silver.account.repository.AccountRepository;
import com.bank.silver.account.service.AccountService;
import com.bank.silver.account.service.TransactionService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTransferUnitTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private AccountService accountService;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        sender = new User("john", "password123", "john@example.com");
        receiver = new User("john2", "password123", "john2@example.com");
    }

    @Test
    void transfer_shouldMoveMoneyBetweenAccountsAndAddTransactions() {
        //given
        Account from = new Account("111111111111", BigDecimal.valueOf(1000), sender);
        Account to = new Account("222222222222", BigDecimal.valueOf(500), receiver);

        when(accountRepository.findByAccountNumber("111111111111"))
                .thenReturn(Optional.of(from));
        when(accountRepository.findByAccountNumber("222222222222"))
                .thenReturn(Optional.of(to));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(inv -> inv.getArgument(0, Account.class));

        //when
        accountService.transfer("111111111111", "222222222222", BigDecimal.valueOf(200));

        //then
        assertThat(from.getBalance()).isEqualByComparingTo("800");
        assertThat(to.getBalance()).isEqualByComparingTo("700");

        verify(transactionService).recordTransaction(from, BigDecimal.valueOf(200), TransactionType.TRANSFER_OUT);
        verify(transactionService).recordTransaction(to, BigDecimal.valueOf(200), TransactionType.TRANSFER_IN);
    }

}
