package com.maxgot.habit_tracker.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabitDailyDto {

    private Long id;
    private String name;
    private boolean completed;
}
