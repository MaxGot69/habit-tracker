package com.maxgot.habit_tracker.service;

import com.maxgot.habit_tracker.dto.HabitStatsResponse;
import com.maxgot.habit_tracker.entity.Habit;
import com.maxgot.habit_tracker.entity.Record;
import com.maxgot.habit_tracker.entity.User;
import com.maxgot.habit_tracker.repository.HabitRepository;
import com.maxgot.habit_tracker.repository.RecordRepository;
import com.maxgot.habit_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private RecordRepository recordRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private StatsService statsService;

    private User user;
    private Habit habit;
    private List<Record> records;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        habit = new Habit();
        habit.setId(1L);
        habit.setName("Test Habit");
        habit.setUser(user);

        LocalDate today = LocalDate.now();
        records = List.of(
                new Record(habit),
                new Record(habit),
                new Record(habit)
        );
        records.get(0).setDate(today.minusDays(0));
        records.get(1).setDate(today.minusDays(1));
        records.get(2).setDate(today.minusDays(2));

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
    }

    @Test
    void getStats_ShouldReturnCorrectStats() {
        when(habitRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(habit));
        when(recordRepository.findByHabitIdAndDateBetween(anyLong(), any(), any())).thenReturn(records);

        HabitStatsResponse result = statsService.getStats(1L);

        assertNotNull(result);
        assertEquals(3, result.getTotalCompletions());
        assertEquals(10.0, result.getCompletionRate(), 0.1);
    }

    @Test
    void getDailyStats_ShouldReturnCorrectDailyStats() {
        when(habitRepository.findByUser(user)).thenReturn(List.of(habit));
        when(recordRepository.existsByHabitIdAndDate(1L, LocalDate.now())).thenReturn(false);

        var result = statsService.getDailyStats();

        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getDate());
        assertEquals(1, result.getHabits().size());
        assertFalse(result.getHabits().get(0).isCompleted());
    }

    @Test
    void getWeekStats_ShouldReturnCorrectWeekStats() {
        when(habitRepository.findByUser(user)).thenReturn(List.of(habit));
        when(recordRepository.existsByHabitIdAndDate(anyLong(), any())).thenReturn(false);

        var result = statsService.getWeekStats();

        assertNotNull(result);
        assertEquals(7, result.getWeek().size());
    }
}