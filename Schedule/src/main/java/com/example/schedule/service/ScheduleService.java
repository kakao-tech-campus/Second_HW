package com.example.schedule.service;

import com.example.exception.ApplicationException;
import com.example.schedule.dto.request.ScheduleSaveRequestDto;
import com.example.schedule.dto.request.ScheduleUpdateRequestDto;
import com.example.schedule.dto.response.ScheduleResponseDto;
import com.example.schedule.entity.User;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.UserRepository;
import com.example.schedule.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public ScheduleResponseDto save(ScheduleSaveRequestDto requestDto) {
        User user = userRepository.findByEmailAndPassword(requestDto.getMemberEmail(), requestDto.getPassword()).orElseThrow(
                () -> new ApplicationException("해당 정보와 일치하는 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );

        Schedule schedule = new Schedule(
                requestDto.getTask(),
                user
        );

        Schedule savedSchedule = scheduleRepository.save(schedule);

        return new ScheduleResponseDto(
                savedSchedule.getId(),
                savedSchedule.getTask(),
                user.getName(),
                user.getEmail(),
                savedSchedule.getCreatedAt(),
                savedSchedule.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new ApplicationException("해당 id의 스케줄이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );

        User user = userRepository.findById(schedule.getUser().getId()).orElseThrow(
                () -> new ApplicationException("해당 id의 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );

        return new ScheduleResponseDto(
                schedule.getId(),
                schedule.getTask(),
                user.getName(),
                user.getEmail(),
                schedule.getCreatedAt(),
                schedule.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public Page<ScheduleResponseDto> findAll(String updatedDate, String memberName, Long memberId, int page, int size) {
        Page<Schedule> schedulePage = scheduleRepository.findAll(updatedDate, memberName, memberId, page, size);
        return schedulePage.map(schedule -> {
            User user = schedule.getUser();
            return new ScheduleResponseDto(
                    schedule.getId(),
                    schedule.getTask(),
                    user.getName(),
                    user.getEmail(),
                    schedule.getCreatedAt(),
                    schedule.getUpdatedAt()
            );
        });
    }

    @Transactional
    public ScheduleResponseDto updateSchedule(Long id, ScheduleUpdateRequestDto requestDto) {
        User user = userRepository.findByEmailAndPassword(requestDto.getMemberEmail(), requestDto.getPassword()).orElseThrow(
                () -> new ApplicationException("해당 정보와 일치하는 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );

        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new ApplicationException("해당 id의 스케줄이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new ApplicationException("해당 스케줄의 작성자가 아닙니다.", HttpStatus.FORBIDDEN);
        }

        schedule.update(requestDto.getTask());
        Schedule updatedSchedule = scheduleRepository.update(schedule);

        return new ScheduleResponseDto(
                updatedSchedule.getId(),
                updatedSchedule.getTask(),
                user.getName(),
                user.getEmail(),
                updatedSchedule.getCreatedAt(),
                updatedSchedule.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteSchedule(Long id, String memberName, String password) {
        User user = userRepository.findByEmailAndPassword(memberName, password).orElseThrow(
                () -> new ApplicationException("해당 정보와 일치하는 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );

        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new ApplicationException("해당 id의 스케줄이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new ApplicationException("해당 스케줄의 작성자가 아닙니다.", HttpStatus.FORBIDDEN);
        }

        scheduleRepository.deleteById(id);
    }
}
