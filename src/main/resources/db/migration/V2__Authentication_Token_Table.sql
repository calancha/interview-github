CREATE TABLE authentication_token (
    payload varchar(255) NOT NULL,
    user_id varchar(255) NOT NULL,
    PRIMARY KEY (payload),
    FOREIGN KEY (user_id) REFERENCES user(id)
) DEFAULT CHARSET=utf8;
