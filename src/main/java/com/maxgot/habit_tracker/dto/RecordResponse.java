package com.maxgot.habit_tracker.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/*
ответ при отметке выполнения
 */
@Getter
@Setter
public class RecordResponse {

    private Long id;

    private Long habitId;

    private Instant date;
}
