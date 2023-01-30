CREATE TABLE IF NOT EXISTS genre
(
    id bigint NOT NULL AUTO_INCREMENT,
    genre_name varchar,
    CONSTRAINT genre_pkey PRIMARY KEY (id)
)

CREATE TABLE IF NOT EXISTS mpa
(
    id bigint NOT NULL AUTO_INCREMENT,
    mpa_name varchar,
    description varchar,
    CONSTRAINT mpa_pkey PRIMARY KEY (id)
)


CREATE TABLE IF NOT EXISTS films
(
    id long NOT NULL AUTO_INCREMENT,
    film_name varchar,
    description varchar,
    release_date date,
    duration bigint,
    genre_id bigint,
    mpa_id bigint,
    CONSTRAINT films_pkey PRIMARY KEY (id),
    CONSTRAINT film_genre FOREIGN KEY (genre_id),
    CONSTRAINT film_mpa FOREIGN KEY (mpa_id)
        REFERENCES genre (id)
)
CREATE TABLE IF NOT EXISTS "user"
(
    user_id bigint NOT NULL AUTO_INCREMENT,
    user_name varchar,
    login varchar,
    email varchar,
    birthday date,
    CONSTRAINT user_pkey PRIMARY KEY (user_id)
)


CREATE TABLE IF NOT EXISTS friendship
(
    friendship_id bigint NOT NULL AUTO_INCREMENT,
    friend1_id bigint,
    friend2_id bigint,
    friendship_status varchar,
    CONSTRAINT friendship_pkey PRIMARY KEY (friendship_id),
    CONSTRAINT user_friendship1 FOREIGN KEY (friend1_id),
    CONSTRAINT user_friendship2 FOREIGN KEY (friend2_id)
)

CREATE TABLE IF NOT EXISTS film_like
(
    film_like_id bigint NOT NULL AUTO_INCREMENT,
    film_id bigint,
    user_id bigint,
    CONSTRAINT film_like_pkey PRIMARY KEY (film_like_id),
    CONSTRAINT film_like_film FOREIGN KEY (film_id),
    CONSTRAINT film_like_user FOREIGN KEY (user_id)

)

CREATE TABLE IF NOT EXISTS film_genre
(
    film_genre_id bigint NOT NULL AUTO_INCREMENT,
    film_id bigint,
    genre_id bigint,
    CONSTRAINT film_genre_pkey PRIMARY KEY (film_genre_id),
    CONSTRAINT film_genre_film FOREIGN KEY (film_id),
    CONSTRAINT film_genre_genre FOREIGN KEY (genre_id)
)
