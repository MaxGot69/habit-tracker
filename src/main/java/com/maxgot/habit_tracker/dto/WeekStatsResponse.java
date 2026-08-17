package com.maxgot.habit_tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WeekStatsResponse {

    private List<WeekDayDto> week;
}
