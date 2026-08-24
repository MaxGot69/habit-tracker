package com.maxgot.habit_tracker.controller;

import com.maxgot.habit_tracker.dto.HabitRequest;
import com.maxgot.habit_tracker.dto.HabitResponse;
import com.maxgot.habit_tracker.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {
    @Autowired
    private HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @Operation(summary = "Создать привычку", description = "Создаёт новую привычку для текущего пользователя")
    @ApiResponse(responseCode = "201", description = "Привычка создана")
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @PostMapping
    public ResponseEntity<HabitResponse> create(@Valid @RequestBody HabitRequest request) {
        HabitResponse created = habitService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
}

    @Operation(summary = "Получить привычку по ID", description = "Возвращает привычку текущего пользователя по её ID")
    @ApiResponse(responseCode = "200", description = "Привычка найдена")
    @ApiResponse(responseCode = "404", description = "Привычка не найдена")
    @GetMapping("/{id}")
        public ResponseEntity<HabitResponse> getById(@PathVariable Long id) {
        HabitResponse habit = habitService.getById(id);
        return ResponseEntity.ok(habit);
    }

    @Operation(summary = "Получить все привычки", description = "Возвращает список привычек текущего пользователя")
    @ApiResponse(responseCode = "200", description = "Список привычек")
    @GetMapping
        public ResponseEntity<List<HabitResponse>> getAll(){
        List<HabitResponse> habit = habitService.getAll();
        return ResponseEntity.ok(habit);
    }

    @Operation(summary = "Обновить привычку", description = "Обновляет существующую привычку текущего пользователя")
    @ApiResponse(responseCode = "200", description = "Привычка обновлена")
    @ApiResponse(responseCode = "404", description = "Привычка не найдена")
    @PutMapping("/{id}")
     public ResponseEntity<HabitResponse> update(@PathVariable Long id, @RequestBody HabitRequest request) {
        HabitResponse habit = habitService.update(id, request);
        return ResponseEntity.ok(habit);
    }

    @Operation(summary = "Удалить привычку", description = "Удаляет привычку текущего пользователя")
    @ApiResponse(responseCode = "204", description = "Привычка удалена")
    @ApiResponse(responseCode = "404", description = "Привычка не найдена")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        habitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}