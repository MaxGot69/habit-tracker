package com.maxgot.habit_tracker.mapper;

import com.maxgot.habit_tracker.dto.RecordResponse;
import com.maxgot.habit_tracker.entity.Record;
import org.springframework.stereotype.Component;

@Component
public class RecordMapper {
    public RecordResponse toResponse(Record record) {
        RecordResponse response = new RecordResponse();
        response.setId(record.getId());
        response.setHabitId(record.getHabit().getId());
        response.setDate(record.getDate());
        return response;
    }


}