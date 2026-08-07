package com.maxgot.habit_tracker.service;

import com.maxgot.habit_tracker.dto.RecordResponse;
import com.maxgot.habit_tracker.exception.HabitNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import com.maxgot.habit_tracker.entity.Record;

@Service
public class RecordService {
    private Map<Long, List<Record>> records = new HashMap<>();
    private final HabitService habitService;
    private AtomicLong idCounter = new AtomicLong(1);
    Long habitId;

    public RecordService(HabitService habitService) {
        this.habitService = habitService;
    }

    public RecordResponse createRecord(Long habitId) {
        habitService.getById(habitId);
           Record record = new Record(habitId);
            record.setId(idCounter.getAndIncrement());
            List<Record> list = this.records.get(habitId);
            if(list == null) {
                list = new ArrayList<>();
            }
                list.add(record);
                this.records.put(habitId,list);
                RecordResponse response = new RecordResponse();
                response.setId(record.getId());
                response.setHabitId(record.getHabitId());
                response.setDate(record.getDate());
        return response;
    }

    public List<RecordResponse> getRecordsByHabitId(Long habitId) {
        habitService.getById(habitId);
        List<Record> list = this.records.get(habitId);
        List<RecordResponse> responses = new ArrayList<>();
        if (list != null) {
            for (Record record : list) {
                RecordResponse response = new RecordResponse();
                response.setId(record.getId());
                response.setHabitId(record.getHabitId());
                response.setDate(record.getDate());
            }
        }
        return responses;
    }
}

