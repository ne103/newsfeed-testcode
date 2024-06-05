package org.example.newsfeed.scheduler;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.repository.PostRepository;
import org.springframework.cglib.core.Local;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional //
public class Scheduler {

    private final PostRepository postRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void deletePost() {

        Timestamp now = new Timestamp(System.currentTimeMillis());
        LocalDate nowDate = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long daysBetween;

        List<Post> posts = postRepository.findByDeleted(Boolean.TRUE);

        for (Post post : posts) {
            Timestamp updatedTime = post.getUpdatedAt();
            LocalDate updatedDate = updatedTime.toInstant().atZone(ZoneId.systemDefault())
                .toLocalDate();
            daysBetween = ChronoUnit.DAYS.between(updatedDate, nowDate);

            if (daysBetween >= 90) {

                postRepository.delete(post);
            }
        }


    }
}
