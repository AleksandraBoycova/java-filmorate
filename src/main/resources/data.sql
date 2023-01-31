INSERT INTO genre(
	id, genre_name)
	VALUES (1, 'comedy')
	VALUES (2, 'drama')
	VALUES (3, 'cartoon')
	VALUES (4, 'thriller')
	VALUES (5, 'documentary')
	VALUES (6, 'action_movie');

INSERT INTO MPA(
    id, mpa_name, description)
    	VALUES (1, 'G', 'у фильма нет возрастных ограничений')
    	VALUES (2, 'PG', 'детям рекомендуется смотреть фильм с родителями')
    	VALUES (3, 'PG_13', 'детям до 13 лет просмотр не желателен')
    	VALUES (4, 'R', 'лицам до 17 лет просматривать фильм можно только в присутствии взрослого')
    	VALUES (5, 'NC_17', 'лицам до 18 лет просмотр запрещён');
