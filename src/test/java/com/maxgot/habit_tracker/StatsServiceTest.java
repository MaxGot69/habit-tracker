package com.maxgot.habit_tracker;

import com.maxgot.habit_tracker.dto.HabitStatsResponse;
import com.maxgot.habit_tracker.entity.Habit;
import com.maxgot.habit_tracker.entity.Record;
import com.maxgot.habit_tracker.repository.HabitRepository;
import com.maxgot.habit_tracker.repository.RecordRepository;
import com.maxgot.habit_tracker.service.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StatsServiceTest {

    @Autowired
    private StatsService statsService;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private RecordRepository recordRepository;

    private Habit habit;

    @BeforeEach
    void setUp() {
        habit = new Habit();
        habit.setName("Test Habit");
        habit.setDescription("Test Description");
        habit.setTarget(1);
        habit = habitRepository.save(habit);
    }

    @Test
    void currentStreakCalculatedCorrectly() {
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 3; i++) {
            Record record = new Record(habit);
            record.setDate(today.minusDays(i));
            recordRepository.save(record);
        }

        HabitStatsResponse response = statsService.getStats(habit.getId());

        assertEquals(3, response.getCurrentStreak());
    }

    @Test
    void currentStreakBreaksOnMissedDay() {
        LocalDate today = LocalDate.now();
        for (int i = 1; i <= 2; i++) {
            Record record = new Record(habit);
            record.setDate(today.minusDays(i));
            recordRepository.save(record);
        }

        HabitStatsResponse response = statsService.getStats(habit.getId());

        assertEquals(0, response.getCurrentStreak());
    }

    @Test
    void bestStreakFindsMaximum() {
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 3; i++) {
            Record record = new Record(habit);
            record.setDate(today.minusDays(i));
            recordRepository.save(record);
        }
        for (int i = 5; i < 7; i++) {
            Record record = new Record(habit);
            record.setDate(today.minusDays(i));
            recordRepository.save(record);
        }

        HabitStatsResponse response = statsService.getStats(habit.getId());

        assertEquals(3, response.getBestStreak());
    }

    @Test
    void completionRateCalculatedCorrectly() {
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 15; i++) {
            Record record = new Record(habit);
            record.setDate(today.minusDays(i * 2));
            recordRepository.save(record);
        }

        HabitStatsResponse response = statsService.getStats(habit.getId());

        assertEquals(50.0, response.getCompletionRate(), 1.0);
    }
}