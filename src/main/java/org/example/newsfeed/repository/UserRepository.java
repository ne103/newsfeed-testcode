package org.example.newsfeed.repository;

import java.util.Optional;
import org.example.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserIdAndStatus(String userId, String status);
    Optional<User> findByUserId(String userId);
    User findByUserIdAndWithdrawn(String userId, boolean withdrawn);
}
