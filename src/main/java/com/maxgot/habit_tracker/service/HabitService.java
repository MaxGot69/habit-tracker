package com.maxgot.habit_tracker.service;

import com.maxgot.habit_tracker.dto.HabitRequest;
import com.maxgot.habit_tracker.dto.HabitResponse;
import com.maxgot.habit_tracker.entity.Habit;
import com.maxgot.habit_tracker.exception.HabitNotFoundException;
import com.maxgot.habit_tracker.repository.HabitRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class HabitService {
    AtomicLong idCounter = new AtomicLong(1);
    Long id;

    private final HabitRepository habitRepository;

    public HabitService(HabitRepository habitRepository){
        this.habitRepository = habitRepository;
    }

    public HabitResponse create(HabitRequest request) {

        Habit habit = new Habit();
        //TODO:возможно заменить на modeleMapper или MapStruct
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setTarget(request.getTarget());
        habit.setCreatedAt(Instant.now());

        Habit saved = habitRepository.save(habit);

        HabitResponse response = new HabitResponse();
        response.setId(habit.getId());
        response.setName(habit.getName());
        response.setDescription(habit.getDescription());
        response.setTarget(habit.getTarget());
        response.setCreatedAt(habit.getCreatedAt());
        return response;
    }

    public HabitResponse getById(Long id) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + id));

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
        for (Habit habit : habitRepository.findAll()) {
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
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + id));
        HabitResponse response = new HabitResponse();
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setTarget(request.getTarget());
        habitRepository.save(habit);
        response.setId(habit.getId());
        response.setName(habit.getName());
        response.setDescription(habit.getDescription());
        response.setTarget(habit.getTarget());
        response.setCreatedAt(habit.getCreatedAt());
        return response;
    }

    public void delete(Long id) {
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + id));
        habitRepository.deleteById(id);
    }
}
