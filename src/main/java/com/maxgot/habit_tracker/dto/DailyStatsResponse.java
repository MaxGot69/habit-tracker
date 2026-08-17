package com.maxgot.habit_tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class DailyStatsResponse {

    private LocalDate date;

    private List<HabitDailyDto> habits;
}
