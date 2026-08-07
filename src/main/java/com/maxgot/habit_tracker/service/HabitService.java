package com.maxgot.habit_tracker.service;

import com.maxgot.habit_tracker.dto.HabitRequest;
import com.maxgot.habit_tracker.dto.HabitResponse;
import com.maxgot.habit_tracker.entity.Habit;
import com.maxgot.habit_tracker.exception.HabitNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class HabitService {
    Map<Long, Habit> habits = new HashMap<>();
    AtomicLong idCounter = new AtomicLong(1);
    Long id;

    public HabitResponse create(HabitRequest request) {

        Habit habit = new Habit();
        //TODO:возможно заменить на modeleMapper или MapStruct
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setTarget(request.getTarget());
        habit.setCreatedAt(Instant.now());

        id = idCounter.getAndIncrement();//генер новый id в -> map
        habit.setId(id);
        habits.put(id, habit);
        //преобраз Habit -> HabitResponse
        HabitResponse response = new HabitResponse();
        response.setId(habit.getId());
        response.setName(habit.getName());
        response.setDescription(habit.getDescription());
        response.setTarget(habit.getTarget());
        response.setCreatedAt(habit.getCreatedAt());
        return response;
    }

    public HabitResponse getById(Long id) {
        Habit habit = habits.get(id);
        if(habit == null) {
            throw new HabitNotFoundException("Habit not found with id: " + id);
        }
            HabitResponse response = new HabitResponse();
            response.setId(habit.getId());
            response.setName(habit.getName());
            response.setDescription(habit.getDescription());
            response.setTarget(habit.getTarget());
            response.setCreatedAt(habit.getCreatedAt());
            return response;
    }

    public List<HabitResponse> getAll(){
        List<HabitResponse> responses = new ArrayList<>();
        for (Habit habit : habits.values()) {
            HabitResponse response = new HabitResponse();
            response.setId(habit.getId());
            response.setName(habit.getName());
            response.setDescription(habit.getDescription());
            response.setTarget(habit.getTarget());
            response.setCreatedAt(habit.getCreatedAt());
            responses.add(response);
        }
        return responses;
    }

    public HabitResponse update(Long id, HabitRequest request) {
        Habit habit = habits.get(id);
        if(habit == null) {
            throw new HabitNotFoundException("Habit not found with id: " + id);
        }
        HabitResponse response = new HabitResponse();
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setTarget(request.getTarget());
        habits.put(id, habit);
        response.setId(habit.getId());
        response.setName(habit.getName());
        response.setDescription(habit.getDescription());
        response.setTarget(habit.getTarget());
        response.setCreatedAt(habit.getCreatedAt());
        return response;
    }

    public void delete(Long id) {
        Habit habit = habits.get(id);
        if(habit == null) {
            throw new HabitNotFoundException("Habit not found with id: " + id);
        }
        habits.remove(id);
    }
}
