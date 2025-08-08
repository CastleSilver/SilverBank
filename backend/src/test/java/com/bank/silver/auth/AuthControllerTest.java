package com.bank.silver.auth;

import com.bank.silver.exception.LoginFailedException;
import com.bank.silver.security.JWTUtils;
import com.bank.silver.user.DTO.LoginRequest;
import com.bank.silver.user.controller.AuthController;
import com.bank.silver.user.entity.User;
import com.bank.silver.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    UserService userService;
    @MockitoBean
    JWTUtils jwtUtils;

    @Test
    void loginSuccess_returnsToken() throws Exception {
        User user = new User("john", "$hashed$", "john@example.com");
        given(userService.login(any(LoginRequest.class))).willReturn(user);
        given(jwtUtils.generateToken("john")).willReturn("header.payload.signature");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"john\",\"password\": \"raw\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("header.payload.signature"))
                .andExpect(jsonPath("$.username").value("john"));

    }

    @Test
    void loginFail_returnsUnauthorized() throws Exception {
        given(userService.login(any())).willThrow(new LoginFailedException());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"john\",\"password\": \"bad\"}"))
                .andExpect(status().isUnauthorized());
    }
}
