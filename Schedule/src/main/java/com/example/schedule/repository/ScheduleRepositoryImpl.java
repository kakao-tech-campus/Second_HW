package com.example.schedule.repository;

import com.example.exception.ApplicationException;
import com.example.schedule.entity.User;
import com.example.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Schedule save(Schedule schedule) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "INSERT INTO schedule (task, member_id, created_at, updated_at) VALUES (?, ?, ?, ?)";
        Timestamp now = Timestamp.valueOf(java.time.LocalDateTime.now());
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, schedule.getTask());
            ps.setLong(2, schedule.getUser().getId());
            ps.setTimestamp(3, now);
            ps.setTimestamp(4, now);
            return ps;
        }, keyHolder);
        schedule.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return findById(schedule.getId()).orElseThrow(
                () -> new ApplicationException("해당 id의 스케줄이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        String sql = "SELECT " +
                "s.id, " +
                "s.task, " +
                "s.created_at, " +
                "s.updated_at, " +
                "m.id AS member_id, " +
                "m.name, " +
                "m.email " +
                "FROM schedule s " +
                "JOIN member m ON s.member_id = m.id " +
                "WHERE s.id = ?";
        List<Schedule> list = jdbcTemplate.query(sql, (rs, rowNum) -> {
            User user = new User(
                    rs.getLong("member_id"),
                    rs.getString("name"),
                    rs.getString("email")
            );
            return new Schedule(
                    rs.getLong("id"),
                    rs.getString("task"),
                    user,
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime()
            );
        }, id);
        return list.stream().findAny();
    }

    @Override
    public Page<Schedule> findAll(String updatedDate, String memberName, Long memberId, int page, int size) {
        StringBuilder querySql = new StringBuilder(
                "SELECT " +
                        "s.id, " +
                        "s.task, " +
                        "s.created_at, " +
                        "s.updated_at, " +
                        "m.id AS member_id, " +
                        "m.name, " +
                        "m.email, " +
                        "COUNT(*) OVER() AS total_count " +
                        "FROM schedule s " +
                        "JOIN member m ON s.member_id = m.id " +
                        "WHERE 1=1 "
        );
        List<Object> params = new ArrayList<>();
        if (updatedDate != null && !updatedDate.isEmpty()) {
            querySql.append("AND DATE(s.updated_at) = ? ");
            params.add(updatedDate);
        }
        if (memberName != null && !memberName.isEmpty()) {
            querySql.append("AND m.name = ? ");
            params.add(memberName);
        }
        if (memberId != null) {
            querySql.append("AND m.id = ? ");
            params.add(memberId);
        }
        querySql.append("ORDER BY s.updated_at DESC ");
        querySql.append("LIMIT ? OFFSET ? ");
        params.add(size);
        params.add(page * size);

        return jdbcTemplate.query(querySql.toString(), rs -> {
            List<Schedule> schedules = new ArrayList<>();
            int total = 0;
            while (rs.next()) {
                if (total == 0) {
                    total = rs.getInt("total_count");
                }
                User user = new User(
                        rs.getLong("member_id"),
                        rs.getString("name"),
                        rs.getString("email")
                );
                Schedule schedule = new Schedule(
                        rs.getLong("id"),
                        rs.getString("task"),
                        user,
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
                schedules.add(schedule);
            }
            return new PageImpl<>(schedules, PageRequest.of(page, size), total);
        }, params.toArray());
    }

    @Override
    public Schedule update(Schedule schedule) {
        String sql = "UPDATE schedule SET task = ?, updated_at = ? WHERE id = ?";
        schedule.setUpdatedAt(java.time.LocalDateTime.now());
        jdbcTemplate.update(sql, schedule.getTask(), Timestamp.valueOf(schedule.getUpdatedAt()), schedule.getId());
        return findById(schedule.getId()).orElseThrow(
                () -> new ApplicationException("해당 id의 스케줄이 존재하지 않습니다.", HttpStatus.NOT_FOUND)
        );
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM schedule WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
