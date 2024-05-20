DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS notices CASCADE;

# 유저 테이블
CREATE TABLE users
(
    user_id           INTEGER PRIMARY KEY AUTO_INCREMENT,
    username          VARCHAR(255)                                                                   NOT NULL,
    email             VARCHAR(255)                                                                   NOT NULL,
    password          VARCHAR(255)                                                                   NOT NULL,
    nickname          VARCHAR(255)                                                                   NULL,
    profile_image     VARCHAR(500)                                                                   NULL,
    provider_type     enum ('LOCAL', 'GOOGLE', 'GITHUB', 'KAKAO', 'NAVER') DEFAULT 'LOCAL'           NOT NULL,
    role_type         enum ('USER', 'ADMIN')                               DEFAULT 'USER'            NOT NULL,
    is_email_verified BOOLEAN                                              DEFAULT FALSE             NOT NULL,
    is_locked         BOOLEAN                                              DEFAULT FALSE             NOT NULL,
    created_at        TIMESTAMP                                            DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at        TIMESTAMP                                            DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT users_email_unique UNIQUE (email),
    CONSTRAINT users_username_unique UNIQUE (username)
);

CREATE VIEW tour_with_address AS
(
SELECT T.tour_id,
       T.content_type_id,
       TC.name AS content_type,
       T.name,
       address,
       zip_code,
       background_image,
       C.name  AS city,
       O.name  AS town
FROM tour T
         LEFT JOIN tour_content TC ON T.content_type_id = TC.content_id
         LEFT JOIN city C ON T.city_code = C.city_code
         LEFT JOIN town O ON O.town_code = T.town_code AND O.city_code = T.city_code
    );


DROP VIEW tour_with_content;
CREATE VIEW tour_with_content AS
(
SELECT T.tour_id,
       T.content_type_id,
       TC.name     AS content_type,
       T.name,
       address,
       zip_code,
       background_image,
       C.name      AS city,
       C.city_code AS city_code,
       O.name      AS town,
       TD.description,
       TD.telephone,
       TD.latitude,
       TD.longitude
FROM tour T
         LEFT JOIN tour_content TC ON T.content_type_id = TC.content_id
         LEFT JOIN city C ON T.city_code = C.city_code
         LEFT JOIN town O ON O.town_code = T.town_code AND O.city_code = T.city_code
         LEFT JOIN tour_detail TD ON T.tour_id = TD.tour_id
    );

CREATE TABLE direction
(
    direction_id  INTEGER PRIMARY KEY AUTO_INCREMENT,
    start_tour_id INTEGER NOT NULL,
    end_tour_id   INTEGER NOT NULL
);

DROP TABLE IF EXISTS vehicle CASCADE;
CREATE TABLE vehicle
(
    vehicle_id     INTEGER PRIMARY KEY AUTO_INCREMENT,
    direction_id   INTEGER      NOT NULL,
    fare           INTEGER      NOT NULL DEFAULT 0,
    spent_time     INTEGER      NOT NULL DEFAULT 0,
    walk_time      INTEGER      NOT NULL DEFAULT 0,
    transfer_count INTEGER      NOT NULL DEFAULT 0,
    distance       INTEGER      NOT NULL DEFAULT 0,
    walk_distance  INTEGER      NOT NULL DEFAULT 0,
    path           VARCHAR(255) NOT NULL,
    CONSTRAINT vehicle_direction_id_fk FOREIGN KEY (direction_id) REFERENCES direction (direction_id)
);

DROP TABLE IF EXISTS step CASCADE;
CREATE TABLE step
(
    step_id      INTEGER PRIMARY KEY AUTO_INCREMENT,
    vehicle_id   INTEGER      NOT NULL,
    mode         VARCHAR(255) NOT NULL,
    start_name   VARCHAR(255) NOT NULL,
    end_name     VARCHAR(255) NOT NULL,
    section_time INTEGER      NULL,
    distance     INTEGER      NOT NULL,
    route_name   VARCHAR(255) NULL,
    CONSTRAINT step_vehicle_id_fk FOREIGN KEY (vehicle_id) REFERENCES vehicle (vehicle_id)
);

SELECT * FROM users;