package org.example.newsfeed.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
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

    @Column
    private String name;

    @Email
    @Column(unique = true)
    private String email;

    @Column
    private String comment;

    @Column
    private String status;

    @Column
    private String refreshToken;

    @Column
    private String statusChangeTime;

    //회원 탈퇴
    @Column
    private boolean deleted;

    @CreatedDate
    private Timestamp createDate;

    @LastModifiedDate
    private Timestamp modifyDate;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private ArrayList<Newsfeed> newsfeeds;



    public User(String userId, String password,
        String  status) {
        this.userId = userId;
        this.password = password;
        this.status = status;
    }



}
