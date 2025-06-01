package com.example.schedule.repository;

import com.example.exception.ApplicationException;
import com.example.schedule.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO member (name, email, password, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setTimestamp(4, Timestamp.valueOf(now));
            ps.setTimestamp(5, Timestamp.valueOf(now));
            return ps;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return findById(user.getId()).orElseThrow(
                () -> new ApplicationException("해당 id의 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        String sql = "SELECT id, name, email, created_at, updated_at FROM member WHERE email = ? AND password = ?";
        List<User> list = jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        ), email, password);
        return list.stream().findAny();
    }

    @Override
    public boolean existByEmail(String email) {
        String sql = "SELECT EXISTS(SELECT 1 FROM member WHERE email = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, email));
    }


    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT id, name, email, password, created_at, updated_at FROM member WHERE id = ?";
        List<User> list = jdbcTemplate.query(sql, (rs, rowNum) -> new User(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("updated_at").toLocalDateTime()
        ), id);
        return list.stream().findAny();
    }

    @Override
    public User update(Long id, User user) {
        String sql = "UPDATE member SET name = ?, email = ?, password = ?, updated_at = ? WHERE id = ?";
        LocalDateTime now = LocalDateTime.now();
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getPassword(), now, id);
        return findById(id).orElseThrow(
                () -> new ApplicationException("해당 id의 회원이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
