package com.example.schedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ScheduleSaveRequestDto {

    @NotBlank(message = "task는 필수값입니다.")
    private String task;
    @NotBlank(message = "memberEmail은 필수값입니다.")
    private String memberEmail;
    @NotBlank(message = "password는 필수값입니다.")
    private String password;
}
