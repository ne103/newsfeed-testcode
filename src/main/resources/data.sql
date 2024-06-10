INSERT INTO user (user_id,password,name,email,comment,status,status_change_time,create_date,modify_date)
VALUES ('abc1234','1234','이병익','mice2008@naver.com','내용테스트','정상',current_timestamp ,current_timestamp,CURRENT_TIMESTAMP);
-- INSERT INTO user (id, name) VALUES ('Team Standup', '2024-06-02T09:00:00');
--
-- DELETE FROM newsfeed WHERE user_id = 'abc1234';


INSERT INTO post(id, created_at, updated_at, content, deleted, user_id)
VALUES(3,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'안녕하세요',false,3);;

INSERT INTO post(id, created_at, updated_at, content, deleted, user_id)
VALUES(5,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'내용테스트',false,3);;