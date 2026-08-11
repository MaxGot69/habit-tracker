package com.maxgot.habit_tracker.repository;

import com.maxgot.habit_tracker.entity.Habit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitRepository extends JpaRepository<Habit, Long> {
}
