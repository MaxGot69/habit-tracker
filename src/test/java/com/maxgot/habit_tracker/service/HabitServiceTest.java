package com.maxgot.habit_tracker.service;

import com.maxgot.habit_tracker.dto.HabitRequest;
import com.maxgot.habit_tracker.dto.HabitResponse;
import com.maxgot.habit_tracker.entity.Habit;
import com.maxgot.habit_tracker.entity.User;
import com.maxgot.habit_tracker.exception.HabitNotFoundException;
import com.maxgot.habit_tracker.mapper.HabitMapper;
import com.maxgot.habit_tracker.repository.HabitRepository;
import com.maxgot.habit_tracker.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HabitServiceTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HabitMapper habitMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private HabitService habitService;

    private User user;
    private Habit habit;
    private HabitRequest request;
    private HabitResponse response;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        habit = new Habit();
        habit.setId(1L);
        habit.setName("Test Habit");
        habit.setDescription("Test Description");
        habit.setTarget(7);
        habit.setCreatedAt(Instant.now());
        habit.setUser(user);

        request = new HabitRequest();
        request.setName("Test Habit");
        request.setDescription("Test Description");
        request.setTarget(7);

        response = new HabitResponse();
        response.setId(1L);
        response.setName("Test Habit");
        response.setDescription("Test Description");
        response.setTarget(7);
        response.setCreatedAt(Instant.now());

        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
    }

    @Test
    void create_ShouldReturnHabitResponse() {
        when(habitMapper.toEntity(request)).thenReturn(habit);
        when(habitRepository.save(any(Habit.class))).thenReturn(habit);
        when(habitMapper.toResponse(habit)).thenReturn(response);

        HabitResponse result = habitService.create(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Habit", result.getName());
        verify(habitRepository).save(any(Habit.class));
    }

    @Test
    void getById_WhenExists_ShouldReturnHabitResponse() {
        when(habitRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(habit));
        when(habitMapper.toResponse(habit)).thenReturn(response);

        HabitResponse result = habitService.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Habit", result.getName());
    }

    @Test
    void getById_WhenNotExists_ShouldThrowException() {
        when(habitRepository.findByIdAndUser(99L, user)).thenReturn(Optional.empty());

        assertThrows(HabitNotFoundException.class, () -> habitService.getById(99L));
    }

    @Test
    void getAll_ShouldReturnListOfHabits() {
        when(habitRepository.findByUser(user)).thenReturn(List.of(habit));
        when(habitMapper.toResponse(habit)).thenReturn(response);

        List<HabitResponse> result = habitService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Habit", result.get(0).getName());
    }

    @Test
    void update_WhenExists_ShouldReturnUpdatedHabit() {
        when(habitRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(habit));
        when(habitMapper.toResponse(habit)).thenReturn(response);

        HabitResponse result = habitService.update(1L, request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(habitRepository).save(habit);
    }

    @Test
    void delete_WhenExists_ShouldDeleteHabit() {
        when(habitRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(habit));

        assertDoesNotThrow(() -> habitService.delete(1L));
        verify(habitRepository).delete(habit);
    }
}