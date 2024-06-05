package org.example.newsfeed.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
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
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Java에서는 Camel case 적용, name설정함으로써 sql에서는 Snake case 적용
    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private ArrayList<Newsfeed> newsfeeds;


    public User(String userId, String password, String email) {
        this.userId = userId ;
        this.password = password;
        this.email = email;

    }


}
