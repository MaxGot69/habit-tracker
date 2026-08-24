package com.maxgot.habit_tracker.controller;

import com.maxgot.habit_tracker.dto.RecordResponse;
import com.maxgot.habit_tracker.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Отметить выполнение привычки",
            description = "Создаёт запись о выполнении привычки за сегодня")
    @ApiResponse(responseCode = "201", description = "Запись создана")
    @ApiResponse(responseCode = "404", description = "Привычка не найдена")
    @PostMapping("/{id}/records")
        public ResponseEntity<RecordResponse> createRecord(@PathVariable Long id) {
        RecordResponse record = recordService.createRecord(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(record);
    }

    @Operation(summary = "Получить все записи привычки",
            description = "Возвращает список всех записей выполнения для указанной привычки")
    @ApiResponse(responseCode = "201", description = "Список записей")
    @ApiResponse(responseCode = "404", description = "Привычка не найдена")
    @GetMapping("/{id}/records")
    public ResponseEntity<List<RecordResponse>> getRecordsByHabitId(@PathVariable Long id) {
        List<RecordResponse> record = recordService.getRecordsByHabitId(id);
        return ResponseEntity.ok(record);
    }
}


