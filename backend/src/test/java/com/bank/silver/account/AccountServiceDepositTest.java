package com.bank.silver.account;

import com.bank.silver.account.dto.response.AccountTransactionResponse;
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
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceDepositTest {

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
    void deposit_shouldIncreaseBalance() {
        //given
        Account account = new Account("123456789012", new BigDecimal("1000"), savedUser);

        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(account));

        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> {
                    Account temp = invocation.getArgument(0, Account.class);
                    return temp;
                });

        //when
        AccountTransactionResponse transactionResponse = accountService.deposit("123456789012", new BigDecimal("500"));

        //then
        assertThat(transactionResponse.balance()).isEqualTo(new BigDecimal("1500"));
    }

}
