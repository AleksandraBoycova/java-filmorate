package ru.yandex.practicum.filmorate.util;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreRowMapper implements RowMapper<Genre>{
    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(rs.getLong("GENRE_ID"));
        genre.setName(rs.getString("GENRE_NAME"));
        return genre;
    }
}
