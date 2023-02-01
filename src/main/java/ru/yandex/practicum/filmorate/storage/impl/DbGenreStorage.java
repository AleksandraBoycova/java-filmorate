package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.util.GenreRowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

@Component
public class DbGenreStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public DbGenreStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre create(Genre genre) {
        String sqlQuery = "insert into genre(genre_name) " +
                "values (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
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
            updateStatement += "genre_name=?";
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
        Optional<Genre> genre = getById(id);
        if (genre.isEmpty()) {
            throw new RuntimeException();
        }
        String deleteStatement = "DELETE FROM genre WHERE id=?";
        jdbcTemplate.update(deleteStatement, id);
        return genre.get();
    }

    @Override
    public Collection<Genre> getAll() {
        String selectStatement = "SELECT genre_name FROM genre";
        return jdbcTemplate.queryForList(selectStatement, Genre.class);
    }

    @Override
    public Optional<Genre> getById(Long id) throws Exception {
        String selectStatement = "SELECT genre_name FROM genre WHERE id=?";
        Genre genre = jdbcTemplate.queryForObject(selectStatement, new GenreRowMapper(), id);
        if (genre == null) {
            return Optional.empty();
        }
        return Optional.of(genre);
    }
}
