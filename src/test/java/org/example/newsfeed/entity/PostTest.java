package org.example.newsfeed.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostTest {

    @Mock
    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 초기화
        post = Post.builder()
            .user(user)
            .content("테스트 포스트")
            .build();
    }

    @Test
    @DisplayName("Post - Post 생성 테스트")
    void test1() {
        // Given
        // setUp() 메서드에서 주어짐

        // When
        // 빌더를 이용한 생성 테스트

        // Then
        assertThat(post.getId()).isNull(); // 아직 ID가 할당되지 않았음
        assertThat(post.getUser()).isEqualTo(user);
        assertThat(post.getContent()).isEqualTo("테스트 포스트");
        assertThat(post.isDeleted()).isFalse(); // deleted 필드 초기값 확인
    }

    @Test
    @DisplayName("setDeleted - 포스트 삭제 테스트")
    void test2() {
        // Given
        // setUp() 메서드에서 주어짐
        assertThat(post.isDeleted()).isFalse(); // 초기에는 삭제되지 않아야 함

        // When
        post.setDeleted();

        // Then
        assertThat(post.isDeleted()).isTrue(); // 삭제 플래그가 true로 설정되었는지 확인
    }

    @Test
    @DisplayName("setContent - 내용 수정 테스트")
    void test3() {
        // Given (setUp() 메서드에서 주어짐)
        assertThat(post.getContent()).isEqualTo("테스트 포스트");

        // When
        post.setContent("수정 포스트");

        // Then
        assertThat(post.getContent()).isEqualTo("수정 포스트");
    }
}
