//package org.example.newsfeed;
//
//import jakarta.annotation.PostConstruct;
//import lombok.AllArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@AllArgsConstructor
//public class DataInitializer {
//
//    private JdbcTemplate jdbcTemplate;
//
//    // @Before
//    @PostConstruct
//    public void init() {
//        String sql1 = "INSERT INTO user(id, password,user_id, email,name, status, status_change_time, create_date, modify_date) VALUES(1, '123', 'a','a@a.com', 'name','정상','2024-06-02T09:00:00','2024-06-02T09:00:00','2024-06-02T09:00:00')";
//        jdbcTemplate.update(sql1);
//        String sql2 = "INSERT INTO user(id, password,user_id, email,name, status, status_change_time, create_date, modify_date) VALUES(2, '123', 'b','b@a.com', 'name','정상','2024-06-02T09:00:00','2024-06-02T09:00:00','2024-06-02T09:00:00')";
//        jdbcTemplate.update(sql2);
//    }
//
//}


// init.sql sql파일을 하나 만들기
// h2 db 처음 시작할 때 yml에 option값으로 init.sql 경로를 써주면 test문 전에 init.sql문이 먼저 돌아서 테스트 할 수 있는 환경 구축