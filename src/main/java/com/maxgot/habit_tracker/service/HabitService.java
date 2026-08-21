package com.maxgot.habit_tracker.service;

import com.maxgot.habit_tracker.dto.HabitRequest;
import com.maxgot.habit_tracker.dto.HabitResponse;
import com.maxgot.habit_tracker.entity.Habit;
import com.maxgot.habit_tracker.entity.User;
import com.maxgot.habit_tracker.exception.HabitNotFoundException;
import com.maxgot.habit_tracker.repository.HabitRepository;
import com.maxgot.habit_tracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Optional;


@Service
public class HabitService {
    AtomicLong idCounter = new AtomicLong(1);
    Long id;

    private final HabitRepository habitRepository;
    private final UserRepository userRepository;

    public HabitService(HabitRepository habitRepository, UserRepository userRepository){

        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public HabitResponse create(HabitRequest request) {

        Habit habit = new Habit();
        //TODO:возможно заменить на modeleMapper или MapStruct
        habit.setName(request.getName());
        habit.setDescription(request.getDescription());
        habit.setTarget(request.getTarget());
        habit.setCreatedAt(Instant.now());
        habit.setUser(getCurrentUser()); //юзер
        Habit saved = habitRepository.save(habit);

        HabitResponse response = new HabitResponse();
        response.setId(habit.getId());
        response.setName(habit.getName());
        response.setDescription(habit.getDescription());
        response.setTarget(habit.getTarget());
        response.setCreatedAt(habit.getCreatedAt());
        return response;
    }

    @Transactional(readOnly = true)
    public HabitResponse getById(Long id) {
        Habit habit = habitRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + id));

            HabitResponse response = new HabitResponse();
            response.setId(habit.getId());
            response.setName(habit.getName());
            response.setDescription(habit.getDescription());
            response.setTarget(habit.getTarget());
            response.setCreatedAt(habit.getCreatedAt());
            return response;
    }

    @Transactional(readOnly = true)
    public List<HabitResponse> getAll(){
        User user = getCurrentUser();
        List<Habit> habits = habitRepository.findByUser(user);
        List<HabitResponse> responses = new ArrayList<>();
        for (Habit habit : habits) {
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

    @Transactional
    public HabitResponse update(Long id, HabitRequest request) {
        Habit habit = habitRepository.findByIdAndUser(id, getCurrentUser())
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

    @Transactional
    public void delete(Long id) {
        Habit habit = habitRepository.findByIdAndUser(id, getCurrentUser())
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + id));
        habitRepository.delete(habit);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
