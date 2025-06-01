package com.example.schedule.repository;

import com.example.schedule.entity.Schedule;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ScheduleRepository {
    Schedule save(Schedule schedule);
    Optional<Schedule> findById(Long id);
    Page<Schedule> findAll(String updatedDate, String memberName, Long memberId, int page, int size);
    Schedule update(Schedule schedule);
    void deleteById(Long id);
}
