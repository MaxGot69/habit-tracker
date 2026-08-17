package com.maxgot.habit_tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WeekDayDto {
    private LocalDate date;
    private int totalCount;
    private int completedCount;
}
