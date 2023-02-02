CREATE TABLE IF NOT EXISTS genre
(
    genre_id long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    genre_name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS mpa
(
    mpa_id long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    mpa_name varchar NOT NULL,
    description varchar
);


CREATE TABLE IF NOT EXISTS films
(
    film_id long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    film_name varchar NOT NULL,
    description varchar(200) NOT NULL,
    release_date date NOT NULL,
    duration long,
    mpa_id long,
    CONSTRAINT film_mpa FOREIGN KEY (mpa_id) REFERENCES mpa (mpa_id)
);
CREATE TABLE IF NOT EXISTS "user"
(
    user_id long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_name varchar,
    login varchar NOT NULL,
    email varchar NOT NULL UNIQUE,
    birthday date NOT NULL
);


CREATE TABLE IF NOT EXISTS friendship
(
    friendship_id long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    user_id long,
    friend_id long,
    friendship_status varchar,
    CONSTRAINT user_friendship1 FOREIGN KEY (user_id) REFERENCES "user"(user_id),
    CONSTRAINT user_friendship2 FOREIGN KEY (friend_id) REFERENCES "user"(user_id)
);

CREATE TABLE IF NOT EXISTS film_like
(
    film_like_id long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    film_id long,
    user_id long,
    CONSTRAINT film_like_film FOREIGN KEY (film_id) REFERENCES films(film_id),
    CONSTRAINT film_like_user FOREIGN KEY (user_id) REFERENCES "user"(user_id)

);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_genre_id long NOT NULL AUTO_INCREMENT PRIMARY KEY,
    film_id long,
    genre_id long,
    CONSTRAINT film_genre_film FOREIGN KEY (film_id) REFERENCES films(film_id),
    CONSTRAINT film_genre_genre FOREIGN KEY (genre_id) REFERENCES genre (genre_id)
);