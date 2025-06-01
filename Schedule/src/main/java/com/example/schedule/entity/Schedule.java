package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Schedule {

    @Setter private Long id;
    private String task;
    private User user;
    @Setter private LocalDateTime createdAt;
    @Setter private LocalDateTime updatedAt;

    public Schedule(String task, User user) {
        this.task = task;
        this.user = user;
    }

    public void update(String task) {
        this.task = task;
    }
}
