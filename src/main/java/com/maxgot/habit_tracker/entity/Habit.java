package com.maxgot.habit_tracker.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Entity
@Getter
@Setter
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int target;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    public Habit(){
    }
    public Habit(String name, String description, int target) {
        this.name = name;
        this.description = description;
        this.target = target;
    }
}



