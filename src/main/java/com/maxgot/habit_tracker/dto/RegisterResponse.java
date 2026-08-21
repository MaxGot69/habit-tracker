package com.maxgot.habit_tracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponse {
    String username;

    public RegisterResponse(String username) {
        this.username = username;
    }
}
