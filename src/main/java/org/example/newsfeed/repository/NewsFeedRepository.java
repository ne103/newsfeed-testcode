package org.example.newsfeed.repository;

import java.util.List;
import org.example.newsfeed.entity.NewsFeed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsFeedRepository extends JpaRepository<NewsFeed, Long> {
    List<NewsFeed> findAllByOrderByCreateDateDesc();

}
