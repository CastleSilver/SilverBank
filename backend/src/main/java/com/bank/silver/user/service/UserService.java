package com.bank.silver.user.service;

import com.bank.silver.exception.LoginFailedException;
import com.bank.silver.user.DTO.LoginRequest;
import com.bank.silver.user.entity.User;
import com.bank.silver.user.DTO.UserRegisterRequest;
import com.bank.silver.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User register(UserRegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("username is already taken");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("email is already taken");
        }

        if (request.password().length() < 8) {
            throw new IllegalArgumentException("password_should_be_more_than_8_characters");
        }

        String hashedPssword = passwordEncoder.encode(request.password());
        User user = new User(request.username(), hashedPssword, request.email());
        return userRepository.save(user);
    }

    public User login(LoginRequest request) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(LoginFailedException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new LoginFailedException();
        }

        return user;
    }
}
