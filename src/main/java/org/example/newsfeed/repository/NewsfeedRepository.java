package org.example.newsfeed.repository;

import org.example.newsfeed.entity.Newsfeed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsfeedRepository extends JpaRepository<Newsfeed, Long> {

}
