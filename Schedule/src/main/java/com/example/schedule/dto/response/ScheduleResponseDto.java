package com.example.schedule.dto.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduleResponseDto {

    private final Long id;
    private final String task;
    private final String memberName;
    private final String memberEmail;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ScheduleResponseDto(Long id, String task, String memberName, String memberEmail, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.task = task;
        this.memberName = memberName;
        this.memberEmail = memberEmail;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
