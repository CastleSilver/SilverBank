package com.bank.silver.user.service;

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

        if (request.password().length() < 8) {
            throw new IllegalArgumentException("password_should_be_more_than_8_characters");
        }

        String hashedPssword = passwordEncoder.encode(request.password());
        User user = new User(request.username(), hashedPssword, request.email());
        return userRepository.save(user);
    }
}
