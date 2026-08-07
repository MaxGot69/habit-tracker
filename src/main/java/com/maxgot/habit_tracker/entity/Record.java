package com.maxgot.habit_tracker.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;

@Entity
@Getter
@Setter
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long habitId;

    @Column(name = "date", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant date;

    public Record(){
    }
    public Record(Long habitId) {
        this.habitId = habitId;
    }
}
