
CREATE TABLE IF NOT EXISTS chat_bot
(
    chat_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id    INT  NOT NULL,
    user_request VARCHAR(500) NOT NULL,
    ai_response VARCHAR(500) NOT NULL,
    CONSTRAINT foreign_key_chat_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);