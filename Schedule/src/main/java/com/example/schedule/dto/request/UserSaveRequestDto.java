package com.example.schedule.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserSaveRequestDto {

    @NotBlank(message = "name은 필수값입니다.")
    private String name;
    @NotBlank(message = "email은 필수값입니다.")
    @Email(message = "email 형식이 올바르지 않습니다.")
    private String email;
    @NotBlank(message = "password는 필수값입니다.")
    private String password;
}
