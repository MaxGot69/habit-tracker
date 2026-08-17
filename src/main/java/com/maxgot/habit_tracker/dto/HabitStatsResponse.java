package com.maxgot.habit_tracker.dto;


import lombok.Getter;
import lombok.Setter;

//статистика
@Getter
@Setter
public class HabitStatsResponse {

    private int currentStreak; // текущая серия(дней подряд)

    private int bestStreak; //макс серия

    private double completionRate; //процент выполнения

    private int totalCompletions; //всего выполнений


}
