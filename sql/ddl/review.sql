# 리뷰 테이블

DROP table IF EXISTS review_like;
DROP table IF EXISTS review_comment;
DROP table IF EXISTS review_images;
DROP table IF EXISTS review;
CREATE TABLE IF NOT EXISTS review
(
    review_id  INT PRIMARY KEY AUTO_INCREMENT,
    content    TEXT NOT NULL,
    user_id    INT  NOT NULL,
    tour_id    INT  NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    like_count INT      DEFAULT 0,
    rating     INT CHECK (rating BETWEEN 1 AND 5),
    CONSTRAINT foreign_key_review_author FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_review_tour_id FOREIGN KEY (tour_id) REFERENCES tour (tour_id) ON DELETE CASCADE
);

# 리뷰 사진 테이블
CREATE TABLE IF NOT EXISTS review_images
(
    review_id INT          NOT NULL,
    image     VARCHAR(500) NOT NULL,
    PRIMARY KEY (review_id, image),
    CONSTRAINT foreign_key_review_img_review_id FOREIGN KEY (review_id) REFERENCES review (review_id) ON DELETE CASCADE
);

# 리뷰 댓글 테이블
CREATE TABLE IF NOT EXISTS review_comment
(
    comment_id INT PRIMARY KEY AUTO_INCREMENT,
    review_id  INT  NOT NULL,
    content    TEXT NOT NULL,
    user_id    INT  NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT foreign_key_review_comment_review_id FOREIGN KEY (review_id) REFERENCES review (review_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_review_comment_author FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

# 리뷰 좋아요 테이블
CREATE TABLE IF NOT EXISTS review_like
(
    review_id INT NOT NULL,
    user_id   INT NOT NULL,
    PRIMARY KEY (review_id, user_id),
    CONSTRAINT foreign_key_review_like_review_id FOREIGN KEY (review_id) REFERENCES review (review_id) ON DELETE CASCADE,
    CONSTRAINT foreign_key_review_like_user_id FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

