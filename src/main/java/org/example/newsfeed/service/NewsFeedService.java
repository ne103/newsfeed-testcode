package org.example.newsfeed.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.newsfeed.entity.NewsFeed;
import org.example.newsfeed.repository.NewsFeedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NewsFeedService {

    private final NewsFeedRepository newsFeedRepository;

    public List<NewsFeed> getAllNewsFeeds() {
        return newsFeedRepository.findAllByOrderByCreateDateDesc();
    }
}
