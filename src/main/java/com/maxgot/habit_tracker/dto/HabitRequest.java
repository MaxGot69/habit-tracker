package com.maxgot.habit_tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
/*
для приёма данных от клиента (name, description, target)
 */

@Getter
@Setter
public class HabitRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;

    @Positive(message = "Target must be a positive number")
    private int target;
}
