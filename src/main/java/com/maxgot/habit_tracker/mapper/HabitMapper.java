package com.maxgot.habit_tracker.mapper;

import com.maxgot.habit_tracker.dto.HabitRequest;
import com.maxgot.habit_tracker.dto.HabitResponse;
import com.maxgot.habit_tracker.entity.Habit;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class HabitMapper {

    public HabitResponse toResponse(Habit habit) {
        HabitResponse response = new HabitResponse();
        response.setId(habit.getId());
        response.setName(habit.getName());
        response.setDescription(habit.getDescription());
        response.setTarget(habit.getTarget());
        response.setCreatedAt(habit.getCreatedAt());
        return response;
    }

    public Habit toEntity(HabitRequest request) {
        Habit habit = new Habit();
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setTarget(request.getTarget());
        habit.setCreatedAt(Instant.now());
        return habit;
    }

}
