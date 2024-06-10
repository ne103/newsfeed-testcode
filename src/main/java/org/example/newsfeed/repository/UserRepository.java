package org.example.newsfeed.repository;

import java.util.Optional;
import org.example.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserId(String userId);
    Optional<User> findByUserIdAndStatus(String userId, String status);
    Optional<User> findById(Long id);
}
