package com.bank.silver.auth;

import com.bank.silver.exception.AccountLockedException;
import com.bank.silver.exception.LoginFailedException;
import com.bank.silver.user.DTO.LoginRequest;
import com.bank.silver.user.entity.User;
import com.bank.silver.user.repository.UserRepository;
import com.bank.silver.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceLoginUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void loginSuccess() {
        //given
        User user = new User("john", "$hashed$", "john@example.com");
        given(userRepository.findByUsername("john")).willReturn(Optional.of(user));
        given(passwordEncoder.matches("password123", "$hashed$")).willReturn(true);
        //when
        User result = userService.login(new LoginRequest("john", "password123"));
        //then
        assertThat(result.getUsername()).isEqualTo("john");
        verify(userRepository).findByUsername("john");
    }

    @Test
    void loginFail_wrongPassword_incrementsFailedCount() {
        User user = new User("john", "$hashed$", "john@example.com");
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));
        given(passwordEncoder.matches("wrongPassword", "$hashed$")).willReturn(false);

        assertThatThrownBy(() -> userService.login(new LoginRequest("john", "wrongPassword")))
                .isInstanceOf(LoginFailedException.class);
        assertThat(user.getFailedLoginCount()).isEqualTo(1);
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    void loginFail_userNotFound() {
        given(userRepository.findByUsername("nope")).willReturn(Optional.empty());
        assertThatThrownBy(() -> userService.login(new LoginRequest("nope", "11112222")))
                .isInstanceOf(LoginFailedException.class);
    }

    @Test
    void loginFail_accountLocked() {
        User user = new User("locked", "$hashed$", "locked@example.com");
        user.setLockedUntil(LocalDateTime.now().plusMinutes(10));
        given(userRepository.findByUsername("locked")).willReturn(Optional.of(user));
        assertThatThrownBy(() -> userService.login(new LoginRequest("locked", "password123")))
                .isInstanceOf(AccountLockedException.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void accountLocksAfterMaxFailedAttemps() {
        User user = new User("john", "$hash$", "john@example.com");
        given(userRepository.findByUsername("john")).willReturn(Optional.of(user));
        given(passwordEncoder.matches(any(), any())).willReturn(false);

        for (int i = 0; i < 5; i++) {
            assertThatThrownBy(() -> userService.login(new LoginRequest("john", "password")))
                    .isInstanceOf(LoginFailedException.class);
        }

        assertThat(user.isLocked()).isTrue();
        assertThatThrownBy(() -> userService.login(new LoginRequest("john", "password")))
                .isInstanceOf(AccountLockedException.class);
    }
}
