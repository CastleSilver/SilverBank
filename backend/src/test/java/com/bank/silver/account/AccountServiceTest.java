package com.bank.silver.account;

import com.bank.silver.account.entity.Account;
import com.bank.silver.account.service.AccountService;
import com.bank.silver.account.util.AccountNumberGenerator;
import com.bank.silver.user.DTO.UserRegisterRequest;
import com.bank.silver.user.entity.User;
import com.bank.silver.user.repository.UserRepository;
import com.bank.silver.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.awaitility.Awaitility.given;

@SpringBootTest
@Transactional
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        savedUser = userService.register(
                new UserRegisterRequest("john", "password123", "john@example.com")
        );
    }

    @Test
    void createAccount_success() {
        //when
        Account account = accountService.createAccount(savedUser);

        //then
        assertThat(account.getAccountNumber()).hasSize(12);
        assertThat(account.getBalance()).isEqualTo(0L);
        assertThat(account.getOwner().getUsername()).isEqualTo("john");
    }
}
