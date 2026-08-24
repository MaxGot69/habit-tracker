package com.maxgot.habit_tracker.service;

import com.maxgot.habit_tracker.dto.RecordResponse;
import com.maxgot.habit_tracker.entity.Habit;
import com.maxgot.habit_tracker.entity.User;
import com.maxgot.habit_tracker.exception.HabitNotFoundException;
import com.maxgot.habit_tracker.mapper.RecordMapper;
import com.maxgot.habit_tracker.repository.HabitRepository;
import com.maxgot.habit_tracker.repository.RecordRepository;
import com.maxgot.habit_tracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.maxgot.habit_tracker.entity.Record;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecordService {
    private final RecordRepository recordRepository;
    private final HabitRepository habitRepository;
    private final UserRepository userRepository;
    private final RecordMapper recordMapper;

    public RecordService( RecordRepository recordRepository,
    HabitRepository habitRepository, UserRepository userRepository,
                          RecordMapper recordMapper) {
        this.recordRepository = recordRepository;
        this.habitRepository = habitRepository;
        this.userRepository = userRepository;
        this.recordMapper = recordMapper;
    }

    @Transactional
    public RecordResponse createRecord(Long habitId) {
        Habit habit = habitRepository.findByIdAndUser(habitId, getCurrentUser())
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + habitId));
                Record record = new Record(habit);
                recordRepository.save(record);
                return recordMapper.toResponse(record);
    }

    @Transactional
    public List<RecordResponse> getRecordsByHabitId(Long habitId) {
        Habit habit = habitRepository.findByIdAndUser(habitId, getCurrentUser())
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + habitId));
        List<Record> list = recordRepository.findByHabitId(habitId);
        List<RecordResponse> responses = new ArrayList<>();
            for (Record record : list) {
                RecordResponse response = recordMapper.toResponse(record);
                responses.add(response);
            }
        return responses;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

