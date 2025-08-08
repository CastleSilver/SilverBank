package com.bank.silver.user.controller;

import com.bank.silver.exception.AccountLockedException;
import com.bank.silver.exception.LoginFailedException;
import com.bank.silver.security.JWTUtils;
import com.bank.silver.user.DTO.LoginRequest;
import com.bank.silver.user.entity.User;
import com.bank.silver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        try {
            User user = userService.login(request);
            String token = jwtUtils.generateToken(user.getUsername());
            return ResponseEntity.ok(Map.of("token", token, "username", user.getUsername()));
        } catch (LoginFailedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (AccountLockedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(Map.of("error", e.getMessage()));
        }
    }
}
