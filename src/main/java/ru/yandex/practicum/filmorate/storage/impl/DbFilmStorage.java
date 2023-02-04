package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.util.FilmRowMapper;
import ru.yandex.practicum.filmorate.util.GenreRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;
import java.util.*;

@Component("dbFilmStorage")
public class DbFilmStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbFilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into films(film_name, description, release_date, duration, mpa_id) " +
                "values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());

            if (film.getMpa() == null) {
                ps.setNull(5, Types.BIGINT);
            } else {
                ps.setLong(5, film.getMpa().getId());
            }
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            film.setId(keyHolder.getKey().longValue());
            if (film.getGenre() != null) {
            updateFilmGenreTable(film.getId(), film.getGenre());}
            updateLikes(film);
            return film;
        }
        return null;
    }

    @Override
    public Film update(Film film) throws Exception {
        if (film.getId() == null || !isExists(film.getId())) {
            throw new NotFoundException("Film not found");
        }
        String updateStatement = "UPDATE films SET ";
        String condition       = "WHERE film_id=?";
        String delimiter       = "";

        List<Object> args = new ArrayList<>();

        if (film.getName() != null) {
            updateStatement += delimiter + "film_name=?";
            args.add(film.getName());
        }
        delimiter = ", ";
        if (film.getDescription() != null) {
            updateStatement += delimiter + "description=?";
            args.add(film.getDescription());
        }
        if (film.getReleaseDate() != null) {
            updateStatement += delimiter + "release_date=?";
            args.add(film.getReleaseDate());
        }
        if (film.getDuration() != 0) {
            updateStatement += delimiter + "duration=?";
            args.add(film.getDuration());
        }

        if (film.getMpa() != null) {
            updateStatement += delimiter + "mpa_id=?";
            args.add(film.getMpa().getId());
        }
        updateStatement += condition;
        args.add(film.getId());
        String finalUpdateStatement = updateStatement;
        jdbcTemplate.update(finalUpdateStatement, args.toArray());
        if (film.getGenre() != null) {

        updateFilmGenreTable(film.getId(), film.getGenre());}
updateLikes(film);
        return getById(film.getId()).orElse(null);
    }

    @Override
    public Film delete(Long id) throws Exception {
        if (!isExists(id)) {
            throw new NotFoundException("Film not found");
        }
        Optional<Film> film = getById(id);
        if (film.isEmpty()) {
            throw new NotFoundException("Film not found");
        }
        String deleteStatement = "DELETE FROM films WHERE film_id=?";
        jdbcTemplate.update(deleteStatement, id);
        return film.get();

    }

    @Override
    public Collection<Film> getAll() {
        String     selectStatement = "SELECT films.*, mpa.* FROM films join mpa on mpa.mpa_id = films.mpa_id";
        List<Film> films           = jdbcTemplate.query(selectStatement, new FilmRowMapper());
        films.forEach(film -> film.setGenre(new HashSet<>(getGenreListForFilm(film.getId()))));
        return films;
    }

    @Override
    public Optional<Film> getById(Long id) throws Exception {
        if (!isExists(id)) {
            throw new NotFoundException("Film not found");
        }
        String selectStatement = "SELECT films.*, mpa.* FROM films left join mpa on films.mpa_id = mpa.mpa_id WHERE film_id=?";
        Film   film            = jdbcTemplate.queryForObject(selectStatement, new FilmRowMapper(), id);
        if (film == null) {
            return Optional.empty();
        }
        film.setGenre(new HashSet<>(getGenreListForFilm(id)));
        film.setLikes(new HashSet<>(getFilmLikes(id)));
        return Optional.of(film);
    }

    public boolean isExists(Long id) {
        String s   = "SELECT COUNT(*) FROM films WHERE film_id=?";
        Long   obj = jdbcTemplate.queryForObject(s, Long.class, id);
        if (obj != null) {
            return obj != 0;
        } else {
            return false;
        }
    }

    private List<Genre> getGenreListForFilm(Long id) {
        List<Genre> query = jdbcTemplate.query("SELECT fg.film_id, g.genre_id, g.genre_name FROM film_genre as fg" +
                " left join genre g on g.genre_id = fg.genre_id WHERE film_id=?", new GenreRowMapper(), id);
        return query;
    }

    private void updateFilmGenreTable(Long filmId, Set<Genre> genreSet) {
        String deleteStatement = "DELETE FROM film_genre WHERE film_id=?";
        jdbcTemplate.update(deleteStatement, filmId);

        String createFilmGenreEntry = "INSERT INTO film_genre (film_id, genre_id) VALUES (?, ?)";
        genreSet.forEach(g -> jdbcTemplate.update(createFilmGenreEntry, filmId, g.getId()));
    }

    private void updateLikes(Film film) {
        String statement = "DELETE FROM film_like WHERE film_id=?";
        jdbcTemplate.update(statement, film.getId());
        film.getLikes().forEach(userId -> {
            String s = "INSERT INTO film_like (film_id, user_id) VALUES (?,?)";
            jdbcTemplate.update(s, film.getId(), userId);
        });
    }
    private Collection<Long> getFilmLikes(Long filmId) {
        String s = "SELECT user_id FROM film_like WHERE film_id=?";
        return jdbcTemplate.queryForList(s, Long.class, filmId);
    }
}
