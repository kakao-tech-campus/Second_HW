package com.example.schedule.service;

import com.example.exception.ApplicationException;
import com.example.exception.PasswordMismatchException;
import com.example.schedule.dto.request.UserSaveRequestDto;
import com.example.schedule.dto.request.UserUpdateRequestDto;
import com.example.schedule.dto.response.UserResponseDto;
import com.example.schedule.entity.User;
import com.example.schedule.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponseDto save(UserSaveRequestDto requestDto) {
        if (userRepository.existByEmail(requestDto.getEmail())) {
            throw new ApplicationException("이미 존재하는 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

        User user = new User(
                requestDto.getName(),
                requestDto.getEmail(),
                requestDto.getPassword()
        );

        User savedUser = userRepository.save(user);

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }

    @Transactional(readOnly = true)
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException("해당 id의 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    @Transactional
    public UserResponseDto update(Long id, UserUpdateRequestDto requestDto) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException("해당 id의 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );

        user.update(requestDto.getName(), requestDto.getPassword());

        User updatedUser = userRepository.update(id, user);

        return new UserResponseDto(
                updatedUser.getId(),
                updatedUser.getName(),
                updatedUser.getEmail(),
                updatedUser.getCreatedAt(),
                updatedUser.getUpdatedAt()
        );
    }

    @Transactional
    public void delete(Long id, String password) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException("해당 id의 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );

        if (!password.equals(user.getPassword())) {
            throw new PasswordMismatchException();
        }

        userRepository.deleteById(id);
    }
}
