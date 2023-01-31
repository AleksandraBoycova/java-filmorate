package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.FilmRowMapper;
import ru.yandex.practicum.filmorate.util.UserRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;

public class FilmDbStorageImpl implements FilmDbStorage{

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public FilmDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = "insert into films(name, description, release_date, duration, genre_id, mpa_id) " +
                "values (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setString(5, film.getGenre());
            ps.setString(6, film.getMpa());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            film.setId(keyHolder.getKey().longValue());
            return film;
        }
        return null;
    }

    @Override
    public Film update(Film film) throws Exception {
        String updateStatement = "UPDATE films SET ";
        String condition = "WHERE id=?";
        if(film.getId() == null) {
            throw new FilmNotFoundException("Film not found");
        }

        if (film.getName() != null) {
            updateStatement += "film_name=?";
        }
        if (film.getDescription() != null) {
            updateStatement += "description=?";
        }
        if (film.getReleaseDate()!= null) {
            updateStatement += "release_date=?";
        }
        if (film.getDuration() != 0) {
            updateStatement += "duration=?";
        }

        if (film.getGenre() != null) {
            updateStatement += "genre_id=?";
        }
        if (film.getMpa() != null) {
            updateStatement += "mpa_id=?";
        }
        updateStatement += condition;
        String finalUpdateStatement = updateStatement;
        int update = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(finalUpdateStatement);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setString(5, film.getGenre());
            ps.setString(6, film.getMpa());
            return ps;
        });
        if (update == 0) {
            throw new RuntimeException();

        }

        return null;
    }

    @Override
    public Film delete(Long id) throws Exception {
        Film film = getById(id);
        String deleteStatement = "DELETE FROM films WHERE id=?";
        jdbcTemplate.update(deleteStatement, id);
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        String selectStatement = "SELECT name, description, release_date, duration, genre_id, mpa_id FROM films";
        List<Film> films = jdbcTemplate.queryForList(selectStatement, Film.class);
        return films;
    }

    @Override
    public Film getById(Long id) throws Exception {
        String selectStatement = "SELECT name, description, release_date, duration, genre_id, mpa_id FROM films WHERE id=?";
        Film film = jdbcTemplate.queryForObject(selectStatement, new Object[]{id}, new FilmRowMapper());
        if (film == null) {
            throw new FilmNotFoundException("Film not found");
        }
        return film;
    }
}
