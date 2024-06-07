package org.example.newsfeed.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.newsfeed.dto.UserRequestDTO;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Java에서는 Camel case 적용, name설정함으로써 sql에서는 Snake case 적용
    @Length(min = 10, max = 20)
    @Column(nullable = false, unique = true)
    private String userId;

    @Length(min = 10)
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    // 이메일 값도 중복되면 안되어서 유니크 걸어두었어요
    @Column(unique = true)
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
    private List<Newsfeed> newsfeeds;


    public User(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public void updateUser(UserRequestDTO dto) {
        this.userId = dto.getUserId();
        this.comment = dto.getComment();
    }

    public void updatePassword(UserRequestDTO dto) {
        this.userId = dto.getPassword();
    }
}
