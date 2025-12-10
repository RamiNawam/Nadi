package com.nadi.controller;

import com.nadi.dto.auth.LoginRequestDto;
import com.nadi.dto.auth.LoginResponseDto;
import com.nadi.dto.auth.RegisterRequestDto;
import com.nadi.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private LoginRequestDto loginRequest;
    private RegisterRequestDto registerRequest;
    private LoginResponseDto loginResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequestDto();
        loginRequest.setEmail("john@example.com");
        loginRequest.setPassword("password123");

        registerRequest = new RegisterRequestDto();
        registerRequest.setName("Jane Doe");
        registerRequest.setEmail("jane@example.com");
        registerRequest.setPhone("+1234567890");
        registerRequest.setPassword("password123");
        registerRequest.setAccountType("user");

        loginResponse = new LoginResponseDto();
        loginResponse.setToken("test_token");
        loginResponse.setUserId(UUID.randomUUID());
        loginResponse.setName("John Doe");
        loginResponse.setEmail("john@example.com");
        loginResponse.setAccountType("user");
    }

    @Test
    void testLogin_Success() {
        when(authService.login(any(LoginRequestDto.class))).thenReturn(loginResponse);

        ResponseEntity<LoginResponseDto> response = authController.login(loginRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("john@example.com");
        verify(authService).login(any(LoginRequestDto.class));
    }

    @Test
    void testLogin_Unauthorized() {
        when(authService.login(any(LoginRequestDto.class))).thenThrow(new RuntimeException("Invalid credentials"));

        ResponseEntity<LoginResponseDto> response = authController.login(loginRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    void testRegister_Success() {
        when(authService.register(any(RegisterRequestDto.class))).thenReturn(loginResponse);

        ResponseEntity<LoginResponseDto> response = authController.register(registerRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        verify(authService).register(any(RegisterRequestDto.class));
    }

    @Test
    void testRegister_BadRequest() {
        when(authService.register(any(RegisterRequestDto.class))).thenThrow(new RuntimeException("Email already exists"));

        ResponseEntity<LoginResponseDto> response = authController.register(registerRequest);

        assertThat(response.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    void testHealthCheck() {
        ResponseEntity<String> response = authController.health();

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Auth service is running");
    }
}

