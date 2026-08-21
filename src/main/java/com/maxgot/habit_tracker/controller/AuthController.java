package com.maxgot.habit_tracker.controller;

import com.maxgot.habit_tracker.dto.LoginRequest;
import com.maxgot.habit_tracker.dto.LoginResponse;
import com.maxgot.habit_tracker.dto.RegisterRequest;
import com.maxgot.habit_tracker.dto.RegisterResponse;
import com.maxgot.habit_tracker.service.UserService;
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

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(
            @Valid @RequestBody RegisterRequest request
    ){
        RegisterResponse response = userService.registerUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(
            @Valid @RequestBody LoginRequest request
            ) {
        LoginResponse response = userService.loginUser(request);
        return ResponseEntity.ok(response);
    }
}