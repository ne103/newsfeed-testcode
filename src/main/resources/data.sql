INSERT INTO user (user_id,password,name,email,comment,status,status_change_time,create_date,modify_date)
VALUES ('abc1234','1234','이병익','mice2008@naver.com','내용테스트','정상',current_timestamp ,current_timestamp,CURRENT_TIMESTAMP);
INSERT INTO user (id, name) VALUES ('Team Standup', '2024-06-02T09:00:00');


INSERT INTO post(id,user_id,content,post.deleted,updated_at,created_at)
VALUES(5,1,'스프링시큐리티도 어려워',false,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);
DELETE FROM user WHERE user_id = 'abc1234';
