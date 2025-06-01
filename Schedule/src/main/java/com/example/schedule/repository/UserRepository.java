package com.example.schedule.repository;

import com.example.schedule.entity.User;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmailAndPassword(String email, String password);
    Optional<User> findById(Long id);
    boolean existByEmail(String email);
    User update(Long id, User user);
    void deleteById(Long id);
}
