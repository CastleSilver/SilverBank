package com.bank.silver.user.service;

import com.bank.silver.user.entity.User;
import com.bank.silver.user.DTO.UserRegisterRequest;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private final List<User> userStore = new ArrayList<>(); // temp storage
    public User register(UserRegisterRequest request) {
        if (userStore.stream().anyMatch(u -> u.getUsername().equals(request.username()))) {
            throw new IllegalArgumentException("username is already taken");
        }

        if (request.password().length() < 8) {
            throw new IllegalArgumentException("password_should_be_more_than_8_characters");
        }

        User user = new User(request.username(), request.password(), request.email());
        userStore.add(user);
        return user;
    }
}
