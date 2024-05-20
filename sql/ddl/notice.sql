DROP TABLE IF EXISTS notice CASCADE;
# 공지사항 테이블
CREATE TABLE notice
(
    notice_id  INTEGER PRIMARY KEY AUTO_INCREMENT,
    title      VARCHAR(255)                        NOT NULL,
    content    TEXT                                NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT NULL
);

