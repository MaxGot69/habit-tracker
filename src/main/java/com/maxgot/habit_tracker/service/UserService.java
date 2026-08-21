package com.maxgot.habit_tracker.service;

import com.maxgot.habit_tracker.config.JwtService;
import com.maxgot.habit_tracker.dto.LoginRequest;
import com.maxgot.habit_tracker.dto.LoginResponse;
import com.maxgot.habit_tracker.dto.RegisterRequest;
import com.maxgot.habit_tracker.dto.RegisterResponse;
import com.maxgot.habit_tracker.entity.User;
import com.maxgot.habit_tracker.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

/*
регистрация (хешировать пароль через BCryptPasswordEncoder)
и логин (проверить пароль через passwordEncoder.matches())
 */
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository,
                       JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public RegisterResponse registerUser(RegisterRequest request){
        if (userRepository.existsByUsername(request.getUsername())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Username занят");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Email уже используется"
            );
        }

        String passwordHash = passwordEncoder.encode(request.getPassword());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordHash);

        userRepository.save(user);
        return new RegisterResponse(user.getUsername());
    }

    public LoginResponse loginUser(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "Неверный логин или пароль")
                );
        boolean passwordCorrect = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordCorrect) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Неверный логин или пароль"
            );
        }

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(token, user.getUsername());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден: " + username));

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    Collections.emptyList()
            );
    }
}
