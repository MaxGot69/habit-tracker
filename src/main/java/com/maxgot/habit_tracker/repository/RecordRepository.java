package com.maxgot.habit_tracker.repository;

import com.maxgot.habit_tracker.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import com.maxgot.habit_tracker.entity.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findByHabitId(Long habitId);
    boolean existsByHabitIdAndDate(Long habitId, LocalDate date);

    List<Record> findByHabitIdAndDateBetween(Long habitId, LocalDate start, LocalDate end);
}
