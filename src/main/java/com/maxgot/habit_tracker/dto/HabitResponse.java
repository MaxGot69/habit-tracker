package com.maxgot.habit_tracker.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/*
для отправки данных клиенту
 */
@Getter
@Setter
public class HabitResponse {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @Positive(message = "Target must be a positive number")
    private int target;

    private Instant createdAt;
}
