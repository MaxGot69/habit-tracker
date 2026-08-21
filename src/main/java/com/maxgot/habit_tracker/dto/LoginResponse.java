package com.maxgot.habit_tracker.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {

    public LoginResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }

    private String token;
    private String username;
}
