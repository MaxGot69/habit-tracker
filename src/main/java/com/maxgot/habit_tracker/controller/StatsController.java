package com.maxgot.habit_tracker.controller;

import com.maxgot.habit_tracker.dto.*;
import com.maxgot.habit_tracker.service.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stats")
public class StatsController {
    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/daily")
    public DailyStatsResponse getDailyStats() {
        return statsService.getDailyStats();
    }

    @GetMapping("/week")
    public WeekStatsResponse getWeekStats() {
        return statsService.getWeekStats();
    }

    @GetMapping("/habits/{id}/stats")
    public HabitStatsResponse getStats(@PathVariable Long id) {
        return statsService.getStats(id);
    }

}
