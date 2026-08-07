package com.maxgot.habit_tracker.controller;

import com.maxgot.habit_tracker.dto.RecordResponse;
import com.maxgot.habit_tracker.service.RecordService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    работа с записями
 */
@RestController
@RequestMapping("/api/habits")
public class RecordController {
    private RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @PostMapping("/{id}/records")
        public ResponseEntity<RecordResponse> createRecord(@PathVariable Long id) {
        RecordResponse record = recordService.createRecord(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @GetMapping("/{id}/records")
    public ResponseEntity<List<RecordResponse>> getRecordsByHabitId(@PathVariable Long id) {
        List<RecordResponse> record = recordService.getRecordsByHabitId(id);
        return ResponseEntity.ok(record);
    }
}


