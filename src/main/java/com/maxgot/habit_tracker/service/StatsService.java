package com.maxgot.habit_tracker.service;

import com.maxgot.habit_tracker.dto.*;
import com.maxgot.habit_tracker.entity.Habit;
import com.maxgot.habit_tracker.entity.Record;
import com.maxgot.habit_tracker.entity.User;
import com.maxgot.habit_tracker.exception.HabitNotFoundException;
import com.maxgot.habit_tracker.repository.HabitRepository;
import com.maxgot.habit_tracker.repository.RecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StatsService {
    private final HabitRepository habitRepository;
    private final RecordRepository recordRepository;
    private final CurrentUserService currentUserService;

    public StatsService(HabitRepository habitRepository, RecordRepository recordRepository,
                        CurrentUserService currentUserService) {
        this.habitRepository = habitRepository;
        this.recordRepository = recordRepository;
        this.currentUserService = currentUserService;
    }

    //Ниже метод подсчет серий
    @Transactional(readOnly = true)
    public HabitStatsResponse getStats(Long habitId) {
        int totalCompletions;
        int bestSreak = 0;
        int current = 0;
        //Проверяем сущ привычки
        Habit habit = habitRepository.findByIdAndUser(habitId, currentUserService.getCurrentUser())
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + habitId));

        LocalDate startDate = habit.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate end = LocalDate.now();
        List<Record> records = recordRepository.findByHabitIdAndDateBetween(habitId, startDate, end);

        //подсчитать колво записей в списке
        totalCompletions = records.size();
        //процент выполнения
        long totalDays = ChronoUnit.DAYS.between(habit.getCreatedAt(), LocalDate.now()) + 1;
        double completionRate = (double) totalCompletions / totalDays * 100;

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
        LocalDate currentDay = startDate;
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
        response.setBestStreak(bestSreak);
        return response;
    }

    @Transactional(readOnly = true)
    public DailyStatsResponse getDailyStats() {
        LocalDate today = LocalDate.now();
        User user = currentUserService.getCurrentUser();
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

    @Transactional(readOnly = true)
    public WeekStatsResponse getWeekStats() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.minusDays(6);
        User user = currentUserService.getCurrentUser();
        List<Habit> habits = habitRepository.findByUser(user);

        // Один запрос к БД за все записи за неделю
        List<Record> records = recordRepository.findByHabit_UserAndDateBetween(user, start, today);

        // Группируем по дате: сколько выполнений в каждый день
        Map<LocalDate, Long> completedByDate = records.stream()
                .collect(Collectors.groupingBy(Record::getDate, Collectors.counting()));

        // Проходим по 7 дням
        List<WeekDayDto> weekList = new ArrayList<>();
        int totalHabits = habits.size();

        for (LocalDate date = start; !date.isAfter(today); date = date.plusDays(1)) {
            int completedCount = completedByDate.getOrDefault(date, 0L).intValue();

            WeekDayDto dto = new WeekDayDto();
            dto.setDate(date);
            dto.setTotalCount(totalHabits);
            dto.setCompletedCount(completedCount);
            weekList.add(dto);
        }

        WeekStatsResponse response = new WeekStatsResponse();
        response.setWeek(weekList);
        return response;
    }
}
