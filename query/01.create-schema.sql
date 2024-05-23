# 초기화
CreATE DATABASE ssafytrip;
CREATE USER 'ssafy'@'%' IDENTIFIED BY 'ssafy';
GRANT ALL PRIVILEGES ON ssafytrip.* to 'ssafy'@'%';

USE ssafytrip;

# 여행 카테고리 테이블
CREATE TABLE city
(
    city_code INT         NOT NULL PRIMARY KEY,
    name      VARCHAR(30) NOT NULL
);
CREATE TABLE direction
(
    direction_id  INT AUTO_INCREMENT PRIMARY KEY,
    start_tour_id INT NOT NULL,
    end_tour_id   INT NOT NULL
);
CREATE TABLE notice
(
    notice_id  INT AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255)                        NOT NULL,
    content    text                                NOT NULL,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL
);
CREATE TABLE tour_content
(
    content_id INT AUTO_INCREMENT PRIMARY KEY,
    name       enum (
        'TOURIST_SPOT',
        'STAY',
        'RESTAURANT',
        'CULTURE',
        'SHOW',
        'TRAVEL',
        'SHOPPING',
        'LEISURE'
        )                   NOT NULL,
    kor_name   VARCHAR(255) NOT NULL
);
CREATE TABLE town
(
    town_code INT         NOT NULL,
    name      VARCHAR(30) NOT NULL,
    city_code INT         NOT NULL,
    PRIMARY KEY (town_code, city_code),
    CONSTRAINT foreign_key_town_city_code FOREIGN KEY (city_code) REFERENCES city (city_code) ON DELETE CASCADE
);
CREATE TABLE tour
(
    tour_id          INT AUTO_INCREMENT PRIMARY KEY,
    content_type_id  INT           NOT NULL,
    name             VARCHAR(100)  NOT NULL,
    address          VARCHAR(200)  NOT NULL,
    zip_code         VARCHAR(10)   NULL,
    background_image VARCHAR(200)  NULL,
    city_code        INT           NULL,
    town_code        INT           NULL,
    hit              INT DEFAULT 0 NULL,
    CONSTRAINT foreign_key_tour_city_code FOREIGN KEY (city_code) REFERENCES city (city_code) ON DELETE CASCADE,
    CONSTRAINT foreign_key_tour_content_type_id FOREIGN KEY (content_type_id) REFERENCES tour_content (content_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_tour_town_code FOREIGN KEY (town_code) REFERENCES town (town_code) ON DELETE CASCADE
);
CREATE TABLE tour_detail
(
    tour_id     INT            NOT NULL PRIMARY KEY,
    description text           NOT NULL,
    telephone   VARCHAR(20)    NULL,
    latitude    DECIMAL(11, 8) NULL,
    longitude   DECIMAL(11, 8) NULL,
    CONSTRAINT foreign_key_tour_detail_tour_id FOREIGN KEY (tour_id) REFERENCES tour (tour_id) ON DELETE CASCADE
);
CREATE TABLE users
(
    user_id           INT AUTO_INCREMENT PRIMARY KEY,
    username          VARCHAR(255)                                     NOT NULL,
    email             VARCHAR(255)                                     NOT NULL,
    password          VARCHAR(255)                                     NOT NULL,
    nickname          VARCHAR(255)                                     NULL,
    profile_image     VARCHAR(500)                                     NULL,
    city_code         INT                                              NULL,
    town_code         INT                                              NULL,
    provider_type     enum (
        'LOCAL',
        'GOOGLE',
        'GITHUB',
        'KAKAO',
        'NAVER'
        )                                    DEFAULT 'LOCAL'           NOT NULL,
    role_type         enum ('USER', 'ADMIN') DEFAULT 'USER'            NOT NULL,
    is_email_verified TINYINT(1)             DEFAULT 0                 NOT NULL,
    is_locked         TINYINT(1)             DEFAULT 0                 NOT NULL,
    created_at        timestamp              DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at        timestamp              DEFAULT CURRENT_TIMESTAMP NOT NULL ON
        UPDATE
        CURRENT_TIMESTAMP,
    CONSTRAINT users_email_unique UNIQUE (email),
    CONSTRAINT users_username_unique UNIQUE (username),
    CONSTRAINT foreign_key_user_city FOREIGN KEY (city_code) REFERENCES city (city_code),
    CONSTRAINT foreign_key_user_town FOREIGN KEY (town_code) REFERENCES town (town_code)
);
CREATE TABLE chat_bot
(
    chat_id      INT AUTO_INCREMENT PRIMARY KEY,
    user_id      INT          NOT NULL,
    user_request VARCHAR(500) NOT NULL,
    ai_response  text         NOT NULL,
    CONSTRAINT foreign_key_chat_bot_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);
CREATE TABLE follow
(
    follower_id INT NOT NULL,
    followee_id INT NOT NULL,
    PRIMARY KEY (followee_id, follower_id),
    CONSTRAINT fw_followee FOREIGN KEY (followee_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT fw_follower FOREIGN KEY (follower_id) REFERENCES users (user_id) ON DELETE CASCADE
);
CREATE TABLE review
(
    review_id  INT AUTO_INCREMENT PRIMARY KEY,
    content    text                               NOT NULL,
    user_id    INT                                NOT NULL,
    tour_id    INT                                NOT NULL,
    created_at datetime DEFAULT CURRENT_TIMESTAMP NULL,
    like_count INT      DEFAULT 0                 NULL,
    rating     INT                                NULL,
    updated_at datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON
        UPDATE
        CURRENT_TIMESTAMP,
    CONSTRAINT foreign_key_review_tour FOREIGN KEY (tour_id) REFERENCES tour (tour_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_review_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CHECK (
        `rating` BETWEEN 1 AND 5
        )
);
CREATE TABLE review_comment
(
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    review_id  INT                                NOT NULL,
    content    text                               NOT NULL,
    user_id    INT                                NOT NULL,
    created_at datetime DEFAULT CURRENT_TIMESTAMP NULL,
    CONSTRAINT foreign_key_review_comment_review FOREIGN KEY (review_id) REFERENCES review (review_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_review_comment_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);
CREATE TABLE review_images
(
    review_id INT          NOT NULL,
    image     VARCHAR(500) NOT NULL,
    PRIMARY KEY (review_id, image),
    CONSTRAINT foreign_key_review_images_review FOREIGN KEY (review_id) REFERENCES review (review_id) ON DELETE CASCADE
);
CREATE TABLE review_like
(
    review_id INT NOT NULL,
    user_id   INT NOT NULL,
    PRIMARY KEY (review_id, user_id),
    CONSTRAINT foreign_key_review_like_review FOREIGN KEY (review_id) REFERENCES review (review_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_review_like_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);
CREATE TABLE schedule
(
    schedule_id     INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255)         NOT NULL,
    user_id         INT                  NOT NULL,
    password        VARCHAR(255)         NULL,
    thumbnail_image VARCHAR(500)         NULL,
    start_date      date                 NOT NULL,
    end_date        date                 NOT NULL,
    city_code       INT                  NOT NULL,
    day             INT        DEFAULT 0 NOT NULL,
    public_key      VARCHAR(100)         NULL,
    is_multi        TINYINT(1) DEFAULT 0 NOT NULL,
    is_private      TINYINT(1) DEFAULT 0 NOT NULL,
    is_finished     TINYINT(1) DEFAULT 0 NOT NULL,
    CONSTRAINT foreign_key_city FOREIGN KEY (city_code) REFERENCES city (city_code) ON DELETE CASCADE,
    CONSTRAINT foreign_key_schedule FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);
CREATE TABLE schedule_privilege
(
    schedule_id INT          NOT NULL,
    username    VARCHAR(255) NOT NULL,
    CONSTRAINT foreign_key_schedule_privilege FOREIGN KEY (schedule_id) REFERENCES schedule (schedule_id) ON DELETE CASCADE
);
CREATE TABLE schedule_trip
(
    schedule_trip_id INT AUTO_INCREMENT PRIMARY KEY,
    schedule_id      INT                  NOT NULL,
    tour_id          INT                  NOT NULL,
    day              INT                  NOT NULL,
    `order`          INT                  NOT NULL,
    room             TINYINT(1) DEFAULT 0 NOT NULL,
    CONSTRAINT foreign_key_schedule_trip_schedule FOREIGN KEY (schedule_id) REFERENCES schedule (schedule_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_schedule_trip_tour FOREIGN KEY (tour_id) REFERENCES tour (tour_id) ON DELETE CASCADE
);
CREATE TABLE vehicle
(
    vehicle_id     INT AUTO_INCREMENT PRIMARY KEY,
    direction_id   INT           NOT NULL,
    fare           INT DEFAULT 0 NOT NULL,
    spent_time     INT DEFAULT 0 NOT NULL,
    walk_time      INT DEFAULT 0 NOT NULL,
    transfer_count INT DEFAULT 0 NOT NULL,
    distance       INT DEFAULT 0 NOT NULL,
    walk_distance  INT DEFAULT 0 NOT NULL,
    path           VARCHAR(255)  NOT NULL,
    CONSTRAINT vehicle_direction_id_fk FOREIGN KEY (direction_id) REFERENCES direction (direction_id)
);
CREATE TABLE schedule_vehicle
(
    schedule_vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    schedule_id         INT NOT NULL,
    vehicle_id          INT NOT NULL,
    type                enum (
        'none',
        'bus',
        'walk',
        'metro',
        'bike',
        'car'
        ) DEFAULT 'none'    NOT NULL,
    from_tour_id        INT NOT NULL,
    to_tour_id          INT NOT NULL,
    day                 INT NOT NULL,
    `order`             INT NOT NULL,
    CONSTRAINT foreign_key_schedule_vehicle_from_tour FOREIGN KEY (from_tour_id) REFERENCES tour (tour_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_schedule_vehicle_schedule FOREIGN KEY (schedule_id) REFERENCES schedule (schedule_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_schedule_vehicle_to_tour FOREIGN KEY (to_tour_id) REFERENCES tour (tour_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_schedule_vehicle_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle (vehicle_id) ON DELETE CASCADE
);
CREATE TABLE step
(
    step_id      INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id   INT          NOT NULL,
    mode         VARCHAR(255) NOT NULL,
    start_name   VARCHAR(255) NOT NULL,
    end_name     VARCHAR(255) NOT NULL,
    section_time INT          NULL,
    distance     INT          NOT NULL,
    route_name   VARCHAR(255) NULL,
    CONSTRAINT step_vehicle_id_fk FOREIGN KEY (vehicle_id) REFERENCES vehicle (vehicle_id)
);