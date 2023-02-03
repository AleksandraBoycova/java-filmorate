package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
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
        String updateStatement = "UPDATE genre SET genre_name=? WHERE genre_id=?";
        if(genre.getId() == null) {
            throw new NotFoundException("Genre not found");
        }
        jdbcTemplate.update(updateStatement, genre.getName(), genre.getId());

        return getById(genre.getId()).orElse(null);
    }

    @Override
    public Genre delete(Long id) throws Exception {
        Optional<Genre> genre = getById(id);
        if (genre.isEmpty()) {
            throw new NotFoundException("Genre not found");
        }
        String deleteStatement = "DELETE FROM genre WHERE genre_id=?";
        jdbcTemplate.update(deleteStatement, id);
        String s = "Delete from film_genre where genre_id = ?";
        jdbcTemplate.update(deleteStatement, id);
        return genre.get();
    }

    @Override
    public Collection<Genre> getAll() {
        String selectStatement = "SELECT * FROM genre";
        return jdbcTemplate.query(selectStatement, new GenreRowMapper());
    }

    @Override
    public Optional<Genre> getById(Long id) throws Exception {
        String selectStatement = "SELECT * FROM genre WHERE genre_id=?";
        Genre genre = jdbcTemplate.queryForObject(selectStatement, new GenreRowMapper(), id);
        if (genre == null) {
            return Optional.empty();
        }
        return Optional.of(genre);
    }
}
