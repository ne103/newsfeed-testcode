package org.example.newsfeed.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import org.example.newsfeed.dto.PostRequestDTO;
import org.example.newsfeed.dto.PostResponseDTO;
import org.example.newsfeed.dto.SearchRequestDTO;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostService {

    private PostRepository postRepository;

    public Post createPost(PostRequestDTO dto) {

        var newPost = dto.toEntity();
        return postRepository.save(newPost);
    }

    public Post updatePost(Long postId, PostRequestDTO dto) {

        Post post = postRepository.findByIdAndDeleted(postId, Boolean.FALSE)
            .orElseThrow(IllegalAccessError::new);

        post.setContent(dto.getContent());

        return postRepository.save(post);

    }

    public void deletePost(Long postId) {
        Post post = postRepository.findByIdAndDeleted(postId, Boolean.FALSE)
            .orElseThrow(IllegalAccessError::new);
        post.setDeleted();
        postRepository.save(post);
        //postRepository.delete(post);
    }

    public Post getPost(Long postId) {
        Post post = postRepository.findByIdAndDeleted(postId, Boolean.FALSE)
            .orElseThrow(IllegalAccessError::new);
        return post;

    }

    public Page<PostResponseDTO> getPosts(int page, boolean canSearch, SearchRequestDTO dto) {
        Sort sort = Sort.by(Direction.DESC, "createdAt"); //생성일자 기준 최신순
        Pageable pageable = PageRequest.of(page, 10, sort); //각 페이지 당 뉴스피드 데이터가 10개씩
        Page<Post> posts;
        if (!canSearch) {   //전체조회
            posts = postRepository.findAll(pageable);
        } else {
            posts = postRepository.findAllByCreatedAtGreaterThanEqualAndCreatedAtLessThan(
                firstDateToTimestamp(dto.getFirstDate()), lastDateToTimestamp(dto.getLastDate()),
                pageable);
        }
        return posts.map(post -> new PostResponseDTO(post));
    }
    public Timestamp firstDateToTimestamp(String dateString) {
        return Timestamp.valueOf(LocalDate.parse(dateString).atStartOfDay());
    }
    public Timestamp lastDateToTimestamp(String dateString) {
        return Timestamp.valueOf(LocalDate.parse(dateString).atTime(LocalTime.MAX));
    }
}