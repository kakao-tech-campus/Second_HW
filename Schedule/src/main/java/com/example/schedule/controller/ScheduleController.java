package com.example.schedule.controller;

import com.example.schedule.dto.request.ScheduleSaveRequestDto;
import com.example.schedule.dto.request.ScheduleUpdateRequestDto;
import com.example.schedule.dto.response.ScheduleResponseDto;
import com.example.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @PostMapping("/schedules")
    public ResponseEntity<ScheduleResponseDto> save(
            @Valid @RequestBody ScheduleSaveRequestDto requestDto
    ) {
        return ResponseEntity.ok(scheduleService.save(requestDto));
    }

    @GetMapping("/schedules")
    public ResponseEntity<Page<ScheduleResponseDto>> findAll(
            @RequestParam(required = false) String updatedDate,
            @RequestParam(required = false) String memberName,
            @RequestParam(required = false) Long memberId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ScheduleResponseDto> result = scheduleService.findAll(updatedDate, memberName, memberId, page - 1, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/schedules/{id}")
    public ResponseEntity<ScheduleResponseDto> findScheduleById(@PathVariable Long id) {
        return ResponseEntity.ok(scheduleService.findScheduleById(id));
    }

    @PutMapping("/schedules/{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id,
            @Valid @RequestBody ScheduleUpdateRequestDto request
    ) {
        return ResponseEntity.ok(scheduleService.updateSchedule(id, request));
    }

    @DeleteMapping("/schedules/{id}")
    public void deleteSchedule(@PathVariable Long id, @RequestParam String memberName, @RequestParam String password) {
        scheduleService.deleteSchedule(id, memberName, password);
    }
}
