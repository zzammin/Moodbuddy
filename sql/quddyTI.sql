CREATE TABLE quddy_ti (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          kakao_id BIGINT NOT NULL,
                          happiness_count INT,
                          anger_count INT,
                          disgust_count INT,
                          fear_count INT,
                          neutral_count INT,
                          sadness_count INT,
                          surprise_count INT,
                          daily_count INT,
                          growth_count INT,
                          emotion_count INT,
                          travel_count INT,
                          quddy_ti_type VARCHAR(10)
);

ALTER TABLE quddy_ti ADD COLUMN created_time TIMESTAMP;
ALTER TABLE quddy_ti ADD COLUMN updated_time TIMESTAMP;

INSERT INTO quddy_ti (kakao_id, happiness_count, anger_count, disgust_count, fear_count, neutral_count, sadness_count, surprise_count, daily_count, growth_count, emotion_count, travel_count, quddy_ti_type, created_time, updated_time)
VALUES
    (3601500664, 5, 5, 3, 4, 4, 4, 4, 17, 4, 4, 4, 'PEH', NOW(), NOW());