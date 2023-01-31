package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.util.FilmRowMapper;
import ru.yandex.practicum.filmorate.util.GenreRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;

public class GenreDbStorageImpl implements GenreDbStorage{

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public GenreDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre create(Genre genre) {
        String sqlQuery = "insert into genre(name) " +
                "values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery);
            ps.setString(1, genre.getName());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            genre.setId(keyHolder.getKey().longValue());
            return genre;
        }
        return null;
    }

    @Override
    public Genre update(Genre genre) throws Exception {
        String updateStatement = "UPDATE genre SET ";
        String condition = "WHERE id=?";
        if(genre.getId() == null) {
            throw new FilmNotFoundException("Genre not found");
        }

        if (genre.getName() != null) {
            updateStatement += "film_name=?";
        }

        updateStatement += condition;
        String finalUpdateStatement = updateStatement;
        int update = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(finalUpdateStatement);
            ps.setString(1, genre.getName());
            return ps;
        });
        if (update == 0) {
            throw new RuntimeException();
        }
        return null;
    }

    @Override
    public Genre delete(Long id) throws Exception {
        Genre genre = getById(id);
        String deleteStatement = "DELETE FROM genre WHERE id=?";
        jdbcTemplate.update(deleteStatement, id);
        return genre;
    }

    @Override
    public Collection<Genre> getAll() {
        String selectStatement = "SELECT name FROM genre";
        List<Genre> genres = jdbcTemplate.queryForList(selectStatement, Genre.class);
        return genres;
    }

    @Override
    public Genre getById(Long id) throws Exception {
        String selectStatement = "SELECT name, description FROM genre WHERE id=?";
        Genre genre = jdbcTemplate.queryForObject(selectStatement, new Object[]{id}, new GenreRowMapper());
        if (genre == null) {
            throw new FilmNotFoundException("Genre not found");
        }
        return genre;
    }
}
