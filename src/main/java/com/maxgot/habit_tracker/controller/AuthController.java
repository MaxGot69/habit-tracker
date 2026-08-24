package com.maxgot.habit_tracker.controller;

import com.maxgot.habit_tracker.dto.LoginRequest;
import com.maxgot.habit_tracker.dto.LoginResponse;
import com.maxgot.habit_tracker.dto.RegisterRequest;
import com.maxgot.habit_tracker.dto.RegisterResponse;
import com.maxgot.habit_tracker.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
POST	/api/auth/register	register()	201 Created
POST	/api/auth/login	login()	200 OK
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "Регистрация",
            description = "Регистрирует пользователя")
    @ApiResponse(responseCode = "201", description = "Регистрация успех")
    @ApiResponse(responseCode = "404", description = "Ошибка регистрации")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(
            @Valid @RequestBody RegisterRequest request
    ){
        RegisterResponse response = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(summary = "Логин пользователя",
            description = "Вход пользователя в аккаунт")
    @ApiResponse(responseCode = "201", description = "Пользователь успешно зашел")
    @ApiResponse(responseCode = "404", description = "Ошибка входа")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @Valid @RequestBody LoginRequest request
            ) {
        LoginResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }
}