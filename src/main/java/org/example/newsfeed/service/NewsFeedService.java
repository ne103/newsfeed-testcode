package org.example.newsfeed.service;

import java.util.List;
import org.example.newsfeed.entity.NewsFeed;
import org.example.newsfeed.repository.NewsFeedRepository;
import org.springframework.stereotype.Service;

@Service
public class NewsFeedService {
    private final NewsFeedRepository newsFeedRepository;

    public NewsFeedService(NewsFeedRepository newsFeedRepository) {
        this.newsFeedRepository = newsFeedRepository;
    }

    public List<NewsFeed> getAllNewsFeeds() {
        return newsFeedRepository.findAllByOrderByCreateDateDesc();
    }
}
