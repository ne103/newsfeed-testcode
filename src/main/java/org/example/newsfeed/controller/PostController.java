package org.example.newsfeed.controller;

import lombok.AllArgsConstructor;
import org.example.newsfeed.CommonResponse;
import org.example.newsfeed.dto.ErrorResponseDTO2;
import org.example.newsfeed.dto.PostRequestDTO;
import org.example.newsfeed.dto.PostResponseDTO;
import org.example.newsfeed.dto.SearchRequestDTO;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.exception.InvalidUserException;
import org.example.newsfeed.exception.PostNotFoundException;
import org.example.newsfeed.security.UserDetailsImpl;
import org.example.newsfeed.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    public final PostService postService;

    @PostMapping
    public ResponseEntity postPost(@RequestBody PostRequestDTO dto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Post post = postService.createPost(dto, userDetails.getUser());
        PostResponseDTO response = new PostResponseDTO(post);
        return ResponseEntity.ok(response);
    }

    @GetMapping("{postId}")
    public ResponseEntity getPost(@PathVariable Long postId) {
        ResponseEntity response;
        try {
            Post post = postService.getPost(postId);
            response = ResponseEntity.ok(new PostResponseDTO(post));
        } catch (PostNotFoundException e) {
            response = ResponseEntity.ok().body(
                new ErrorResponseDTO2("403", "게시글 조회에 실패했습니다.", e.getMessage()));
        }

        return response;
    }


    @PutMapping("{postId}")
    public ResponseEntity updatePost(@PathVariable Long postId, @RequestBody PostRequestDTO dto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ResponseEntity response;
        try {
            Post post = postService.updatePost(postId, dto, userDetails.getUser());
            response = ResponseEntity.ok().body(CommonResponse.builder()
                .msg("게시글 수정에 성공했습니다")
                .statusCode(HttpStatus.OK.value())
                .build());
        } catch (InvalidUserException | PostNotFoundException e) {
            response = ResponseEntity.ok().body(
                new ErrorResponseDTO2("403", "게시글 수정에 실패했습니다.", e.getMessage()));
        }

        return response;
    }


    @DeleteMapping("{postId}")
    public ResponseEntity deletePost(@PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        ResponseEntity response;

        try {
            postService.deletePost(postId, userDetails.getUser());
            response = ResponseEntity.ok().body(CommonResponse.builder()
                .msg("게시글 삭제에 성공했습니다")
                .statusCode(HttpStatus.OK.value())
                .build());

        } catch (InvalidUserException | PostNotFoundException e) {
            response = ResponseEntity.ok().body(
                new ErrorResponseDTO2("403", "게시글 삭제에 실패했습니다.", e.getMessage()));
        }

        return response;
    }

    @GetMapping("/page")
    public Page<PostResponseDTO> getPostPage(
        @RequestParam("page") int page, //페이지 번호 1부터
        @RequestParam("search") boolean canSearch,  //기간별 검색 기능 사용할지
        @RequestBody(required = false) SearchRequestDTO dto) { //기간
        return postService.getPosts(page - 1, canSearch, dto);
    }


}
