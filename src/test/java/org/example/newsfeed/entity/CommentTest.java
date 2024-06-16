package org.example.newsfeed.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class CommentTest {

    @Mock
    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
        comment = Comment.builder()
            .user(user)
            .content("테스트 코멘트")
            .build();
    }

    @Test
    @DisplayName("Comment - Comment 생성 테스트")
    void test1() {
        // Given
        // setUp() 메서드에서 주어짐

        // When
        // 생성자와 빌더 모두 테스트

        // Then
        assertThat(comment.getId()).isNull(); // 아직 ID가 할당되지 않음
        assertThat(comment.getUser()).isEqualTo(user);
        assertThat(comment.getContent()).isEqualTo("테스트 코멘트");
        assertThat(comment.getPost()).isNull(); // 아직 포스트가 할당되지 않음
    }

    @Test
    @DisplayName("setContent - 내용 수정 테스트")
    void test2() {
        // Given (setUp() 메서드에서 주어짐)
        assertThat(comment.getContent()).isEqualTo("테스트 코멘트");

        // When
        comment.setContent("수정 코멘트");

        // Then
        assertThat(comment.getContent()).isEqualTo("수정 코멘트");
    }
}
