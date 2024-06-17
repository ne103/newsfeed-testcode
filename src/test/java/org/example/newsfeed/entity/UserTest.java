package org.example.newsfeed.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.example.newsfeed.dto.PasswordRequestDTO;
import org.example.newsfeed.dto.UserRequestDTO;
import org.example.newsfeed.exception.InvalidPasswordException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserTest {

    private User user;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        user = new User(
            "testUserId",
            passwordEncoder.encode("testPassword1!"),
            "testName",
            "testEmail@email.com",
            "test Comment",
            null,
            null,
            UserStatusEnum.ACTIVE
        );
    }

    @Test
    @DisplayName("setStatus - 상태 변경 테스트")
    void test1() {
        // Given
        UserStatusEnum newStatus = UserStatusEnum.ACTIVE;

        // When
        user.setStatus(newStatus);

        // Then
        assertThat(user.getStatus()).isEqualTo(newStatus.getStatus());
    }


    @Test
    @DisplayName("User - User 생성 테스트")
    void test2() {
        // Given
        // setUp() 메서드에서 주어짐

        // When
        // 객체가 생성되면서 진행됨

        // Then
        assertThat(user.getUserId()).isEqualTo("testUserId");
        assertThat(user.getPassword()).isEqualTo(user.getPassword());
        assertThat(user.getName()).isEqualTo("testName");
        assertThat(user.getEmail()).isEqualTo("testEmail@email.com");
        assertThat(user.getComment()).isEqualTo("test Comment");
        assertNull(user.getRefreshToken());
        assertNull(user.getStatusChangeTime());
        assertThat(user.getStatus()).isEqualTo(UserStatusEnum.ACTIVE.getStatus());
    }

    @Test
    @DisplayName("updateUser - Name, Comment 변경 테스트")
    void test3() {
        // Given
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
            .name("김나금")
            .comment("테스트 코멘트")
            .build();

        // When
        user.updateUser(userRequestDTO);

        // Then
        assertThat(user.getName()).isEqualTo("김나금");
        assertThat(user.getComment()).isEqualTo("테스트 코멘트");
    }

    @Test
    @DisplayName("updatePassword - Password 변경 테스트")
    void test4() {
        // Given
        String beforePassword = "testPassword1!";
        String updatePassword = "newPassword1!";
        PasswordRequestDTO dto = new PasswordRequestDTO(beforePassword, updatePassword);

        // When
        user.updatePassword(dto, passwordEncoder);

        // Then
        assertThat(user.getPassword()).isNotEqualTo(beforePassword); // 비밀번호가 업데이트 되었는지 확인
        assertThat(passwordEncoder.matches(updatePassword,
            user.getPassword())).isTrue(); // 업데이트된 비밀번호가 일치하는지 확인

    }

    @Test
    @DisplayName("updatePassword - Password 불일치 테스트")
    void test5() {
        // Given
        String wrongBeforePassword = "TestPassword1!";
        String updatePassword = "newPassword1!";
        PasswordRequestDTO dto = new PasswordRequestDTO(wrongBeforePassword, updatePassword);

        // When, Then
        assertThrows(InvalidPasswordException.class, () -> {
            user.updatePassword(dto, passwordEncoder);
        });
    }
}
