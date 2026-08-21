package com.maxgot.habit_tracker.service;

import com.maxgot.habit_tracker.dto.*;
import com.maxgot.habit_tracker.entity.Habit;
import com.maxgot.habit_tracker.entity.Record;
import com.maxgot.habit_tracker.entity.User;
import com.maxgot.habit_tracker.exception.HabitNotFoundException;
import com.maxgot.habit_tracker.repository.HabitRepository;
import com.maxgot.habit_tracker.repository.RecordRepository;
import com.maxgot.habit_tracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StatsService {
    private final HabitRepository habitRepository;
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    public StatsService(HabitRepository habitRepository, RecordRepository recordRepository,
                        UserRepository userRepository) {
        this.habitRepository = habitRepository;
        this.recordRepository = recordRepository;
        this.userRepository = userRepository;
    }

    //Ниже метод подсчет серий
    public HabitStatsResponse getStats(Long habitId) {
        double completionRate;
        int totalCompletions;
        int bestSreak = 0;
        int current = 0;
        //Проверяем сущ привычки
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + habitId));
        LocalDate start = LocalDate.now().minusDays(30);
        LocalDate end = LocalDate.now();
        List<Record> records = recordRepository.findByHabitIdAndDateBetween(habitId, start, end);

        //подсчитать колво записей в списке
        totalCompletions = records.size();
        //процент выполнения
        completionRate = (double)totalCompletions/30 * 100;

        //список записей лист рекорд в сет локалдэйт
        Set<LocalDate> doneDates = records.stream()
                .map(Record::getDate)
                .collect(Collectors.toSet());

        LocalDate date = LocalDate.now();
        int currentStreak = 0;
        while(doneDates.contains(date)){
            currentStreak++;
            date = date.minusDays(1);
        }
        LocalDate currentDay = start;
        while (!currentDay.isAfter(end)) {
            if(doneDates.contains(currentDay)) {
                current++;
            }else {
                if (current > bestSreak) {
                    bestSreak = current;
                }
                current = 0;
            }
            currentDay = currentDay.plusDays(1);
        }
        if (current > bestSreak) {
            bestSreak = current;
        }
        HabitStatsResponse response = new HabitStatsResponse();
        response.setCurrentStreak(currentStreak);
        response.setTotalCompletions(totalCompletions);
        response.setCompletionRate(completionRate);
        return response;
    }

    public DailyStatsResponse getDailyStats() {
        LocalDate today = LocalDate.now();
        User user = getCurrentUser();
        List<Habit> habits = habitRepository.findByUser(user);
        List<HabitDailyDto> dailyList = new ArrayList<>();

        for (Habit habit : habits) {
            boolean completed = recordRepository.existsByHabitIdAndDate(habit.getId(), today);
            HabitDailyDto dto = new HabitDailyDto();
            dto.setId(habit.getId());
            dto.setName(habit.getName());
            dto.setCompleted(completed);
            dailyList.add(dto);
        }
        DailyStatsResponse response = new DailyStatsResponse();
        response.setDate(today);
        response.setHabits(dailyList);
        return response;
    }

    public WeekStatsResponse getWeekStats() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);
        User user = getCurrentUser();
        List<Habit> habits = habitRepository.findByUser(user);
        List<WeekDayDto> weekList = new ArrayList<>();

        for (LocalDate date = start; !date.isAfter(today); date = date.plusDays(1)) {
            int completedCount = 0;
            for (Habit habit : habits) {
                if (recordRepository.existsByHabitIdAndDate(habit.getId(), date)) {
                    completedCount++;
                }
            }
            WeekDayDto dto = new WeekDayDto();
            dto.setDate(date);
            dto.setTotalCount(habits.size());
            dto.setCompletedCount(completedCount);
            weekList.add(dto);
        }

        WeekStatsResponse response = new WeekStatsResponse();
        response.setWeek(weekList);
        return response;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
