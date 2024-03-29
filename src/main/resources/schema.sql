drop table IF EXISTS film_genre;
drop table IF EXISTS friend;
drop table IF EXISTS like_film;
drop table IF EXISTS users;
drop table IF EXISTS films;
drop table IF EXISTS genre;
drop table IF EXISTS mpa;

create TABLE IF NOT EXISTS mpa
(
    id_mpa   INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name_mpa VARCHAR NOT NULL UNIQUE
);

create TABLE IF NOT EXISTS films
(
    id_film           INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR(255) NOT NULL,
    description  VARCHAR(200),
    release_date DATE,
    duration     INT,
    rate         INT,
    mpa       INT,
    FOREIGN KEY (mpa) REFERENCES mpa (id_mpa) ON delete CASCADE
);

create TABLE IF NOT EXISTS users
(
    id_user       INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    email    VARCHAR(255) NOT NULL,
    login    VARCHAR(255) NOT NULL,
    name     VARCHAR(255),
    birthday DATE         NOT NULL
);


create TABLE IF NOT EXISTS genre
(
    id_genre   INT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

create TABLE IF NOT EXISTS film_genre
(
    id_film  INT,
    id_genre INT,
    FOREIGN KEY (id_genre) REFERENCES genre (id_genre) ON delete CASCADE,
    FOREIGN KEY (id_film) REFERENCES films (id_film) ON delete CASCADE,
    PRIMARY KEY (id_film, id_genre)
);

create TABLE IF NOT EXISTS like_film
(
    user_id INT,
    film_id INT,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES films (id_film) ON delete CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id_user) ON delete CASCADE
);

create TABLE IF NOT EXISTS friend
(
    user1_id   INT,
    user2_id INT,
    FOREIGN KEY (user1_id) REFERENCES users (id_user) ON delete CASCADE,
    FOREIGN KEY (user2_id) REFERENCES users (id_user) ON delete CASCADE,
    PRIMARY KEY (user1_id, user2_id)
);
