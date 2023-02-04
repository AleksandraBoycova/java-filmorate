INSERT INTO genre(genre_name) VALUES ('comedy');
	INSERT INTO genre(genre_name) VALUES ('drama');
	INSERT INTO genre(genre_name) VALUES ('cartoon');
	INSERT INTO genre(genre_name) VALUES ('thriller');
	INSERT INTO genre(genre_name) VALUES ('documentary');
	INSERT INTO genre(genre_name) VALUES ('action_movie');

INSERT INTO MPA(mpa_name, description) VALUES ('G', 'у фильма нет возрастных ограничений');
    	INSERT INTO MPA(mpa_name, description) VALUES ('PG', 'детям рекомендуется смотреть фильм с родителями');
    	INSERT INTO MPA(mpa_name, description) VALUES ('PG_13', 'детям до 13 лет просмотр не желателен');
    	INSERT INTO MPA(mpa_name, description) VALUES ('R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого');
    	INSERT INTO MPA(mpa_name, description) VALUES ('NC_17', 'лицам до 18 лет просмотр запрещён');

 INSERT INTO films (film_name ,description ,    release_date ,    duration ,    mpa_id) VALUES ('name2', 'description2', now(), 120, 1);
 INSERT INTO films (film_name ,description ,    release_date ,    duration ,    mpa_id) VALUES ('name3', 'description3', now(), 120, 1);
 INSERT INTO films (film_name ,description ,    release_date ,    duration ,    mpa_id) VALUES ('name4', 'description4', now(), 120, 1);
 INSERT INTO films (film_name ,description ,    release_date ,    duration ,    mpa_id) VALUES ('name5', 'description5', now(), 120, 1);

 INSERT INTO "user" (    login , user_name ,   email ,    birthday) VALUES( 'login2', 'name2', 'email2@mail.com', now());
 INSERT INTO "user" (    login ,  user_name ,  email ,    birthday) VALUES('login3', 'name3', 'email3@mail.com', now());
 INSERT INTO "user" (    login , user_name ,  email ,    birthday) VALUES('login4', 'name4', 'email4@mail.com', now());
 INSERT INTO "user" (    login ,  user_name ,  email ,    birthday) VALUES('login5', 'name5', 'email5@mail.com', now());

 INSERT INTO film_genre (film_id ,    genre_id ) VALUES (1, 1);
 INSERT INTO film_genre (film_id ,    genre_id ) VALUES (2, 1);
 INSERT INTO film_genre (film_id ,    genre_id ) VALUES (3, 1);
 INSERT INTO film_genre (film_id ,    genre_id ) VALUES (4, 1);
