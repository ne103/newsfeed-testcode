package org.example.newsfeed;


import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        String sql1 = "INSERT INTO post(id, user_id, content, created_At, updated_At, deleted) VALUES(10, 1, '내용', '2024-01-01 00:00:00', '2024-01-01 00:00:00', false)";
        String sql2 = "INSERT INTO post(id, user_id, content, created_At, updated_At, deleted) VALUES(11, 1, '내용', '2024-01-01 00:00:00', '2024-01-01 00:00:00', true)";
        String sql3 = "INSERT INTO post(id, user_id, content, created_At, updated_At, deleted) VALUES(12, 1, '내용', '2024-05-01 00:00:00', '2024-05-05 00:00:00', true)";
        String sql4 = "INSERT INTO post(id, user_id, content, created_At, updated_At, deleted) VALUES(13, 1, '내용', '2024-01-01 00:00:00', '2024-01-01 00:00:00', false)";
        jdbcTemplate.update(sql1);
        jdbcTemplate.update(sql2);
        jdbcTemplate.update(sql3);
        jdbcTemplate.update(sql4);

    }

}
