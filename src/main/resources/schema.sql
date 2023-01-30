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
    CONSTRAINT film_genre FOREIGN KEY (genre_id)
        REFERENCES genre (id)
)
