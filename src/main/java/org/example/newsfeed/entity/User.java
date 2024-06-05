package org.example.newsfeed.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String user_id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    // 이메일 값도 중복되면 안되어서 유니크 걸어두었어요
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String comment;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private String statusChangeTime;

    @CreatedDate
    private Timestamp createDate;

    @LastModifiedDate
    private Timestamp modifyDate;

    public User(String user_id, String password, String email) {
        this.user_id = user_id;
        this.password = password;
        this.email = email;

    }


}
