DROP TABLE IF EXISTS schedule;
CREATE TABLE schedule
(
    schedule_id     INT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(255)          NOT NULL,
    user_id         INT                   NOT NULL,
    password        VARCHAR(255)          NULL,
    thumbnail_image VARCHAR(500)          NULL,
    start_date      DATE                  NOT NULL,
    end_date        DATE                  NOT NULL,
    city_code       INT                   NOT NULL,
    is_multi        BOOLEAN DEFAULT FALSE NOT NULL,
    is_private      BOOLEAN DEFAULT FALSE NOT NULL,
    is_finished     BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT foreign_key_schedule FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_city FOREIGN KEY (city_code) REFERENCES city (city_code) ON DELETE CASCADE
);

CREATE TABLE schedule_privilege
(
    schedule_id INT          NOT NULL,
    username    VARCHAR(255) NOT NULL,
    CONSTRAINT foreign_key_schedule_privilege FOREIGN KEY (schedule_id) REFERENCES schedule (schedule_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS schedule_trip;
CREATE TABLE schedule_trip
(
    schedule_trip_id INT AUTO_INCREMENT PRIMARY KEY,
    schedule_id      INT                   NOT NULL,
    tour_id          INT                   NOT NULL,
    day              INT                   NOT NULL,
    `order`          INT                   NOT NULL,
    is_room          BOOLEAN DEFAULT FALSE NOT NULL,
    CONSTRAINT foreign_key_schedule_trip_schedule FOREIGN KEY (schedule_id) REFERENCES schedule (schedule_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_schedule_trip_tour FOREIGN KEY (tour_id) REFERENCES tour (tour_id) ON DELETE CASCADE
);

DROP TABLE IF EXISTS schedule_vehicle;
CREATE TABLE schedule_vehicle
(
    schedule_vehicle_id INT AUTO_INCREMENT PRIMARY KEY,
    schedule_id         INT                                                          NOT NULL,
    vehicle_id          INT                                                          NULL,
    type                ENUM ('none', 'bus', 'walk', 'metro', 'bike') DEFAULT 'none' NOT NULL,
    from_tour_id        INT                                                          NOT NULL,
    to_tour_id          INT                                                          NOT NULL,
    day                 INT                                                          NOT NULL,
    `order`             INT                                                          NOT NULL,
    CONSTRAINT foreign_key_schedule_vehicle_schedule FOREIGN KEY (schedule_id) REFERENCES schedule (schedule_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_schedule_vehicle_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicle (vehicle_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_schedule_vehicle_from_tour FOREIGN KEY (from_tour_id) REFERENCES tour (tour_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_schedule_vehicle_to_tour FOREIGN KEY (to_tour_id) REFERENCES tour (tour_id) ON DELETE CASCADE
);