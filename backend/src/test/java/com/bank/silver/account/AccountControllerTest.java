package com.bank.silver.account;

import com.bank.silver.account.controller.AccountController;
import com.bank.silver.account.dto.response.AccountTransactionResponse;
import com.bank.silver.account.dto.response.TransferResponse;
import com.bank.silver.account.entity.Account;
import com.bank.silver.account.service.AccountService;
import com.bank.silver.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    @Test
    void deposit_shouldReturnUpdatedBalance() throws Exception {
        //given
        Account account = new Account("111111111111", BigDecimal.valueOf(1000), new User("john", "password1234", "john@example.com"));
        when(accountService.deposit("111111111111", BigDecimal.valueOf(200)))
                .thenReturn(new AccountTransactionResponse(account.getAccountNumber(), BigDecimal.valueOf(200), account.getBalance()));

        //when & then
        mockMvc.perform(post("/api/accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":\"111111111111\",\"amount\":200}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("111111111111"))
                .andExpect(jsonPath("$.balance").value(1000));
    }

    @Test
    void withdraw_shouldReturnUpdatedBalance() throws Exception {
        //given
        Account account = new Account("111111111111", BigDecimal.valueOf(800), new User("john", "password123", "john@example.com"));
        when(accountService.withdraw("111111111111", BigDecimal.valueOf(200)))
                .thenReturn(new AccountTransactionResponse(account.getAccountNumber(), BigDecimal.valueOf(200), account.getBalance()));

        //when & then
        mockMvc.perform(post("/api/accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountNumber\":\"111111111111\",\"amount\":200}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(800));
    }

    @Test
    void transfer_shouldMoveMoneyBetweenAccounts() throws Exception {
        //given
        Account from = new Account("111111111111", BigDecimal.valueOf(800), new User("john1", "password123", "john1@example.com"));
        Account to = new Account("222222222222", BigDecimal.valueOf(1200), new User("john2", "password123", "john2@example.com"));

        when(accountService.transfer("111111111111", "222222222222", BigDecimal.valueOf(200)))
                .thenReturn(new TransferResponse(from.getAccountNumber(), BigDecimal.valueOf(200), BigDecimal.valueOf(600),
                        to.getAccountNumber(), BigDecimal.valueOf(1000)));

        //when & then
        mockMvc.perform(post("/api/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fromAccountNumber\":\"111111111111\",\"toAccountNumber\":\"222222222222\",\"amount\":200}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromBalance").value(600))
                .andExpect(jsonPath("$.toBalance").value(1000));
    }
}
