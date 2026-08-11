package com.maxgot.habit_tracker;

import com.maxgot.habit_tracker.dto.HabitRequest;
import com.maxgot.habit_tracker.dto.HabitResponse;
import com.maxgot.habit_tracker.exception.HabitNotFoundException;
import com.maxgot.habit_tracker.service.HabitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class HabitTrackerApplicationTests {

	@Autowired
	private HabitService habitService;

	@Test
	void createHabit_Success() {
		// given
		HabitRequest request = new HabitRequest();
		request.setName("Test Habit");
		request.setDescription("Test Description");
		request.setTarget(10);

		// when
		HabitResponse response = habitService.create(request);

		// then
		assertNotNull(response);
		assertNotNull(response.getId());
		assertEquals("Test Habit", response.getName());
		assertEquals("Test Description", response.getDescription());
		assertEquals(10, response.getTarget());
		assertNotNull(response.getCreatedAt());
	}

	@Test
	void getById_HabitExists_ReturnsHabit() {
		// given
		HabitRequest request = new HabitRequest();
		request.setName("Test Habit");
		request.setDescription("Test Description");
		request.setTarget(10);

		HabitResponse created = habitService.create(request);
		Long id = created.getId();

		// when
		HabitResponse found = habitService.getById(id);

		// then
		assertNotNull(found);
		assertEquals(id, found.getId());
		assertEquals("Test Habit", found.getName());
		assertEquals("Test Description", found.getDescription());
		assertEquals(10, found.getTarget());
	}

	@Test
	void getById_HabitNotFound_ThrowsException() {
		// when & then
		assertThrows(HabitNotFoundException.class, () -> {
			habitService.getById(999L);
		});
	}
}
