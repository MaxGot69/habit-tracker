package com.maxgot.habit_tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank
     String username;
    @NotBlank
     String password;
    @NotBlank
    @Email
        String email;
}
