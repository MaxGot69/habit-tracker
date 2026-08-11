package com.maxgot.habit_tracker.service;

import com.maxgot.habit_tracker.dto.RecordResponse;
import com.maxgot.habit_tracker.entity.Habit;
import com.maxgot.habit_tracker.exception.HabitNotFoundException;
import com.maxgot.habit_tracker.repository.HabitRepository;
import com.maxgot.habit_tracker.repository.RecordRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import com.maxgot.habit_tracker.entity.Record;

@Service
public class RecordService {
    private final RecordRepository recordRepository;
    private final HabitRepository habitRepository;

    public RecordService( RecordRepository recordRepository,
    HabitRepository habitRepository) {
        this.recordRepository = recordRepository;
        this.habitRepository = habitRepository;
    }

    public RecordResponse createRecord(Long habitId) {
        Habit habit = habitRepository.findById(habitId)
                .orElseThrow(() -> new HabitNotFoundException("Habit not found with id: " + habitId));
           Record record = new Record(habit);
                recordRepository.save(record);
                RecordResponse response = new RecordResponse();
                response.setId(record.getId());
                response.setHabitId(record.getHabit().getId());
                response.setDate(record.getDate());
        return response;
    }

    public List<RecordResponse> getRecordsByHabitId(Long habitId) {
        List<Record> list = recordRepository.findByHabitId(habitId);
        List<RecordResponse> responses = new ArrayList<>();
            for (Record record : list) {
                RecordResponse response = new RecordResponse();
                response.setId(record.getId());
                response.setHabitId(record.getHabit().getId());
                response.setDate(record.getDate());
            }
        return responses;
    }
}

