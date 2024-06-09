package org.example.newsfeed.controller;

import java.util.List;
import org.example.newsfeed.dto.NewsFeedResponseDto;
import org.example.newsfeed.entity.NewsFeed;
import org.example.newsfeed.service.NewsFeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class NewsFeedController {

    private final NewsFeedService newsFeedService;

    @Autowired
    public NewsFeedController(NewsFeedService newsFeedService) {
        this.newsFeedService = newsFeedService;
    }

    @GetMapping("/newsfeed")
    public ResponseEntity<?> getAllNewsFeeds() {
        List<NewsFeed> newsFeeds = newsFeedService.getAllNewsFeeds();
        if (newsFeeds.isEmpty()) {
            NewsFeedResponseDto response = new NewsFeedResponseDto(200, "먼저 작성하여 소식을 알려보세요");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(newsFeeds);
    }
}
