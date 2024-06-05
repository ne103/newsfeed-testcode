package org.example.newsfeed.repository;

import java.util.Optional;
import org.example.newsfeed.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.user_id = :userId")
    Optional<User> findByUserId(@Param("userId") String userId);
    Optional<User> findByEmail(String email);
}
