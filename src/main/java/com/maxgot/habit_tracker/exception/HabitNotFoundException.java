package com.maxgot.habit_tracker.exception;

public class HabitNotFoundException extends RuntimeException{

    public HabitNotFoundException(String message){
        super(message);
    }
}
