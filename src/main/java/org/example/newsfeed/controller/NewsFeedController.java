package org.example.newsfeed.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.newsfeed.dto.NewsFeedResponseDto;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.service.NewsFeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class NewsFeedController {

    private final NewsFeedService newsFeedService;

    @GetMapping("/newsfeed")
    public ResponseEntity<?> getAllNewsFeeds() {
        List<Post> newsFeeds = newsFeedService.getAllNewsFeeds();
        if (newsFeeds.isEmpty()) {
            NewsFeedResponseDto response = new NewsFeedResponseDto(200, "먼저 작성하여 소식을 알려보세요");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(newsFeeds);
    }
}
