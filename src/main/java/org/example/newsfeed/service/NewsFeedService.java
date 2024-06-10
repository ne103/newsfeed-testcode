package org.example.newsfeed.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.repository.NewsFeedRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsFeedService {

    private final NewsFeedRepository newsFeedRepository;

    public List<Post> getAllNewsFeeds() {
        return newsFeedRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }
}
