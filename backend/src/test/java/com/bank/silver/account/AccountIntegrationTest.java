package com.bank.silver.account;

import com.bank.silver.account.dto.DepositRequest;
import com.bank.silver.account.dto.TransferRequest;
import com.bank.silver.account.dto.WithdrawRequest;
import com.bank.silver.account.entity.Account;
import com.bank.silver.account.repository.AccountRepository;
import com.bank.silver.user.entity.User;
import com.bank.silver.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AccountIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    private User sender;
    private User receiver;

    @BeforeEach
    void setUp() {
        accountRepository.deleteAll();
        userRepository.deleteAll();

        sender = userRepository.save(new User("john", "password123", "john@example.com"));
        receiver = userRepository.save(new User("jane", "password123", "jane@example.com"));

        accountRepository.save(new Account("111111111111", BigDecimal.valueOf(1000), sender));
        accountRepository.save(new Account("222222222222", BigDecimal.valueOf(500), receiver));
    }

    @Test
    void deposit_shouldIncreaseBalance() throws Exception {
        DepositRequest depositRequest = new DepositRequest("111111111111", BigDecimal.valueOf(200));

        mockMvc.perform(post("/api/accounts/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("111111111111"))
                .andExpect(jsonPath("$.balance").value(1200));

        Account updated = accountRepository.findByAccountNumber("111111111111").get();
        assertThat(updated.getBalance()).isEqualByComparingTo("1200");
    }

    @Test
    void withdraw_shouldDecreaseBalance() throws Exception {
        WithdrawRequest request = new WithdrawRequest("111111111111", BigDecimal.valueOf(200));

        mockMvc.perform(post("/api/accounts/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("111111111111"))
                .andExpect(jsonPath("$.balance").value(800));

        Account updated = accountRepository.findByAccountNumber("111111111111").get();
        assertThat(updated.getBalance()).isEqualByComparingTo("800");
    }

    @Test
    void transfer_shouldMoveMoneyBetweenAccounts() throws Exception {
        TransferRequest request = new TransferRequest("111111111111", "222222222222", BigDecimal.valueOf(200));

        mockMvc.perform(post("/api/accounts/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromAccount").value("111111111111"))
                .andExpect(jsonPath("$.fromBalance").value(800))
                .andExpect(jsonPath("$.toAccount").value("222222222222"))
                .andExpect(jsonPath("$.toBalance").value(700));

        Account from = accountRepository.findByAccountNumber("111111111111").get();
        Account to = accountRepository.findByAccountNumber("222222222222").get();

        assertThat(from.getBalance()).isEqualByComparingTo("800");
        assertThat(to.getBalance()).isEqualByComparingTo("700");
    }

    @Test
    void getAccount_shouldReturnAccountDetailsWithTransactions() throws Exception {
        Account testAccount = accountRepository.findByAccountNumber("111111111111").get();
        mockMvc.perform(get("/api/accounts/{accountNumber}", testAccount.getAccountNumber())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("111111111111"))
                .andExpect(jsonPath("$.balance").value(1000))
                .andExpect(jsonPath("$.ownerName").value("john"))
                .andExpect(jsonPath("$.ownerEmail").value("john@example.com"));

    }

    @Test
    void getMonthlyTransactions_shouldReturnTransactionsForGivenMonth() throws Exception {
        mockMvc.perform(get("/api/accounts/{accountNumber}/transactions", "111111111111")
                        .param("year", "2025")
                        .param("month", "8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("111111111111"))
                .andExpect(jsonPath("$.year").value(2025))
                .andExpect(jsonPath("$.month").value(8))
                .andExpect(jsonPath("$.transactions").isArray());
    }
}
