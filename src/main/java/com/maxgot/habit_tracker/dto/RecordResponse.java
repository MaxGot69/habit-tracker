package com.maxgot.habit_tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

/*
ответ при отметке выполнения
 */
@Getter
@Setter
public class RecordResponse {

    private Long id;

    private Long habitId;

    private LocalDate date;
}
