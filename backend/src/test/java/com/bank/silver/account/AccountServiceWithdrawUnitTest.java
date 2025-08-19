package com.bank.silver.account;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.entity.TransactionType;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceWithdrawUnitTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private User savedUser;

    @BeforeEach
    void setUp() {
        savedUser = new User("john", "password123", "john@example.com");
    }

    @Test
    void withdraw_shouldDecreaseBalanceAndAddTransaction() {
        //given
        Account account = new Account("123456789012", BigDecimal.valueOf(1000), savedUser);
        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(inv -> inv.getArgument(0, Account.class));

        //when
        accountService.withdraw("123456789012", BigDecimal.valueOf(200));

        //then
        assertThat(account.getBalance()).isEqualByComparingTo("800");
        assertThat(account.getTransactions()).hasSize(1);
        assertThat(account.getTransactions().get(0).getType())
                .isEqualTo(TransactionType.WITHDRAW);
    }
}
