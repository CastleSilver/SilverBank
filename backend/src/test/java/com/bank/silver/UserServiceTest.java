package com.bank.silver;

import com.bank.silver.user.entity.User;
import com.bank.silver.user.DTO.UserRegisterRequest;
import com.bank.silver.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserServiceTest {

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService();
    }

    @Test
    void join_success() {
        //given
        var request = new UserRegisterRequest("john", "password123", "john@example.com");

        //when
        User user = userService.register(request);

        //then
        assertThat(user.getUsername()).isEqualTo("john");
        assertThat(user.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void join_duplicate_username_exception() {
        //given
        var request1 = new UserRegisterRequest("john", "password123", "john@example.com");
        var request2 = new UserRegisterRequest("john", "password999", "john2@example.com");

        userService.register(request1);

        //when & then
        assertThatThrownBy(() -> userService.register(request2))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("username is already taken");
    }

    @Test
    void if_password_length_less_than_8_exception() {
        //given
        var request = new UserRegisterRequest("john", "short", "john@exception.com");

        //when & then
        assertThatThrownBy(() -> userService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("password_should_be_more_than_8_characters");
    }
}
