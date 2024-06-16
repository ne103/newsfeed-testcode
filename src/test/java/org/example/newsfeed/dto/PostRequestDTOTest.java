package org.example.newsfeed.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.example.newsfeed.entity.Post;
import org.example.newsfeed.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class PostRequestDTOTest {
    private PostRequestDTO postRequestDTO;

    @Mock
    private User user;

    @BeforeEach
    void setUp() {
            MockitoAnnotations.openMocks(this); // Mockito 초기화
            postRequestDTO = new PostRequestDTO();
            postRequestDTO.setContent("테스트 포스트");
    }

    @Test
    @DisplayName("toEntity() 메서드 테스트")
    void test1() {
        // Given
        when(user.getId()).thenReturn(1L); // 예를 들어 사용자 ID가 1일 때

        // When
        Post post = postRequestDTO.toEntity(user);

        // Then
        assertThat(post).isNotNull();
        assertThat(post.getContent()).isEqualTo("테스트 포스트");
        assertThat(post.getUser()).isEqualTo(user);
    }
}
