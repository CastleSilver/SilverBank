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
public class AccountServiceTransferTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account sender;
    private Account receiver;
    private User sendUser;
    private User receiveUser;

    @BeforeEach
    void setUp() {
        sendUser = new User("john", "password123", "john@example.com");
        receiveUser = new User("ryan", "ryan123456", "ryan@example.com");
        sender = new Account("123456789012", BigDecimal.valueOf(1000), sendUser);
        receiver = new Account("987654321098", BigDecimal.valueOf(500), receiveUser);
    }

    @Test
    void transfer_success() {
        //given
        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("987654321098"))
                .thenReturn(Optional.of(receiver));
        when(accountRepository.save(sender)).thenReturn(sender);
        when(accountRepository.save(receiver)).thenReturn(receiver);

        //when
        accountService.transfer("123456789012", "987654321098", BigDecimal.valueOf(200));

        //then
        assertThat(sender.getBalance()).isEqualByComparingTo("800");
        assertThat(receiver.getBalance()).isEqualByComparingTo("700");
    }

    @Test
    void transfer_fail_if_insufficient_balance() {
        //given
        when(accountRepository.findByAccountNumber("123456789012"))
                .thenReturn(Optional.of(sender));
        when(accountRepository.findByAccountNumber("987654321098"))
                .thenReturn(Optional.of(receiver));

        //when & then
        assertThatThrownBy(() -> accountService.transfer("123456789012", "987654321098", BigDecimal.valueOf(2000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Insufficient balance");
    }

    @Test
    void transfer_fail_if_account_not_found() {
        //given
        when(accountRepository.findByAccountNumber(anyString()))
                .thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> accountService.transfer("111111111111", "987654321098", BigDecimal.valueOf(100)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Account not found");
    }
}
