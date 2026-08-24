package com.maxgot.habit_tracker.service;

import com.maxgot.habit_tracker.dto.LoginRequest;
import com.maxgot.habit_tracker.dto.LoginResponse;
import com.maxgot.habit_tracker.dto.RegisterRequest;
import com.maxgot.habit_tracker.dto.RegisterResponse;
import com.maxgot.habit_tracker.entity.User;
import com.maxgot.habit_tracker.repository.UserRepository;
import com.maxgot.habit_tracker.config.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void register_ShouldSaveUserWithHashedPassword() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("secret");
        request.setEmail("test@example.com");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("hashed_secret");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        RegisterResponse result = userService.registerUser(request);

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_WithCorrectPassword_ShouldReturnToken() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("secret");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashed_secret");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret", "hashed_secret")).thenReturn(true);
        when(jwtService.generateToken("testuser")).thenReturn("jwt_token");

        LoginResponse result = userService.loginUser(request);

        assertNotNull(result);
        assertEquals("jwt_token", result.getToken());
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void login_WithWrongPassword_ShouldThrowException() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashed_secret");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashed_secret")).thenReturn(false);

        assertThrows(ResponseStatusException.class, () -> userService.loginUser(request));
    }
}