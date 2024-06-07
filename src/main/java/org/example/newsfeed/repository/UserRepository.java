package org.example.newsfeed.repository;

import java.util.Optional;
import org.example.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserId(String userId);
}
