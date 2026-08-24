package com.maxgot.habit_tracker.controller;

import com.maxgot.habit_tracker.dto.*;
import com.maxgot.habit_tracker.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Сводка за сегодня",
            description = "Возвращает список всех привычек с флагом выполнена ли она сегодня")
    @ApiResponse(responseCode = "200", description = "Сводка за сегодня")
    @GetMapping("/daily")
    public DailyStatsResponse getDailyStats() {
        return statsService.getDailyStats();
    }

    @Operation(summary = "Прогресс за неделю",
            description = "Возвращает количество выполненных привычек по дням за последние 7 дней")
    @ApiResponse(responseCode = "200", description = "Прогресс за неделю")
    @GetMapping("/week")
    public WeekStatsResponse getWeekStats() {
        return statsService.getWeekStats();
    }

    @Operation(summary = "Статистика по привычке",
            description = "Возвращает серии, процент выполнения и общее количество выполнений для привычки")
    @ApiResponse(responseCode = "200", description = "Статистика найдена")
    @ApiResponse(responseCode = "404", description = "Привычка не найдена")
    @GetMapping("/habits/{id}/stats")
    public HabitStatsResponse getStats(@PathVariable Long id) {
        return statsService.getStats(id);
    }
}