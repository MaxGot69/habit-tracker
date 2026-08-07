package com.maxgot.habit_tracker.controller;

import com.maxgot.habit_tracker.dto.HabitRequest;
import com.maxgot.habit_tracker.dto.HabitResponse;
import com.maxgot.habit_tracker.service.HabitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {
    @Autowired
    private HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @PostMapping
    public ResponseEntity<HabitResponse> create(@Valid @RequestBody HabitRequest request) {
        HabitResponse created = habitService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
}

    @GetMapping("/{id}")
        public ResponseEntity<HabitResponse> getById(@PathVariable Long id) {
        HabitResponse habit = habitService.getById(id);
        return ResponseEntity.ok(habit);
    }

    @GetMapping
        public ResponseEntity<List<HabitResponse>> getAll(){
        List<HabitResponse> habit = habitService.getAll();
        return ResponseEntity.ok(habit);
    }

    @PutMapping("/{id}")
     public ResponseEntity<HabitResponse> update(@PathVariable Long id, @RequestBody HabitRequest request) {
        HabitResponse habit = habitService.update(id, request);
        return ResponseEntity.ok(habit);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        habitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}