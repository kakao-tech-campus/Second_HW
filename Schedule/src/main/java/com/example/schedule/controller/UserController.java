package com.example.schedule.controller;

import com.example.schedule.dto.request.UserSaveRequestDto;
import com.example.schedule.dto.request.UserUpdateRequestDto;
import com.example.schedule.dto.response.UserResponseDto;
import com.example.schedule.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping("/members")
    public ResponseEntity<UserResponseDto> save(
            @Valid @RequestBody UserSaveRequestDto requestDto
    ) {
        return ResponseEntity.ok(userService.save(requestDto));
    }

    @GetMapping("/members/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/members/{id}")
    public ResponseEntity<UserResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequestDto requestDto
    ) {
        return ResponseEntity.ok(userService.update(id, requestDto));
    }

    @DeleteMapping("/members/{id}")
    public void delete(
            @PathVariable Long id,
            @RequestParam String password
    ) {
        userService.delete(id, password);
    }
}
