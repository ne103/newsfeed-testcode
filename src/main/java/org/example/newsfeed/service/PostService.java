package org.example.newsfeed.service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import org.example.newsfeed.dto.PostRequestDTO;
import org.example.newsfeed.dto.PostResponseDTO;
import org.example.newsfeed.dto.SearchRequestDTO;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.exception.InvalidUserException;
import org.example.newsfeed.exception.PostNotFoundException;
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

    public Post createPost(PostRequestDTO dto, User user) {

        var newPost = dto.toEntity(user); //userid 추가해서 post 생성
        return postRepository.save(newPost);
    }

    public Post updatePost(Long postId, PostRequestDTO dto, User user) {

        Post post = postRepository.findByIdAndDeleted(postId, Boolean.FALSE)
            .orElseThrow(()->new PostNotFoundException("해당 게시글이 존재하지 않습니다."));

        if(post.getUser().getId().equals(user.getId())){  //유저 아이디가 일치하면 수정
            post.setContent(dto.getContent());
        }
        else{
            throw new InvalidUserException("작성자가 아닙니다.");//exception
        }


        return postRepository.save(post);

    }

    public void deletePost(Long postId, User user) {
        Post post = postRepository.findByIdAndDeleted(postId, Boolean.FALSE)
            .orElseThrow(()->new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
        if(post.getUser().getId().equals(user.getId())){ //유저 아이디가 일치하면 삭제
            post.setDeleted();
        }
        else{
            throw new InvalidUserException("작성자가 아닙니다.");//exception
        }

        postRepository.save(post);
        //postRepository.delete(post);
    }

    public Post getPost(Long postId) {
        Post post = postRepository.findByIdAndDeleted(postId, Boolean.FALSE)
            .orElseThrow(()->new PostNotFoundException("해당 게시글이 존재하지 않습니다."));
        return post;

    }

    public Page<PostResponseDTO> getPosts(int page, boolean canSearch, SearchRequestDTO dto) {
        Sort sort = Sort.by(Direction.DESC, "createdAt"); //생성일자 기준 최신순
        Pageable pageable = PageRequest.of(page, 10, sort); //각 페이지 당 뉴스피드 데이터가 10개씩
        Page<Post> posts;
        if (!canSearch) {   //전체조회
            posts = postRepository.findAllByDeleted(Boolean.FALSE, pageable); // 삭제 안된 게시물 불러오기
        } else {
            posts = postRepository.findAllByDeletedAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(Boolean.FALSE,
                firstDateToTimestamp(dto.getFirstDate()), lastDateToTimestamp(dto.getLastDate()),
                pageable);  //삭제 안된 게시물, 기간별 검색
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