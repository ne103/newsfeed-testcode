package org.example.newsfeed.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class PostResponseDTOTest {

    @Mock
    private Post post;

    @Mock
    private User user;

    @BeforeEach
    void setUp() {
        // Mocking Post
        when(post.getId()).thenReturn(1L);
        when(post.getUser()).thenReturn(user); // Mocking the getUser() method to return a User mock
        when(post.getContent()).thenReturn("Test Content");
        when(post.getCreatedAt()).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(post.getUpdatedAt()).thenReturn(Timestamp.valueOf(LocalDateTime.now()));

        // Mocking User
        when(user.getUserId()).thenReturn("testUserId");
    }

    @Test
    @DisplayName("PostResponseDTO 생성 테스트")
    void test1() {
        // Given - setUp()에서 주어짐

        // When
        PostResponseDTO dto = new PostResponseDTO(post);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getUserId()).isEqualTo("testUserId");
        assertThat(dto.getContent()).isEqualTo("Test Content");
        assertThat(dto.getPostDate()).isBefore(Instant.now());
        assertThat(dto.getModifiedDate()).isBefore(Instant.now());
    }
}
