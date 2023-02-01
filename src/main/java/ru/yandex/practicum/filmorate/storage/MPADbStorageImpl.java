package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.util.FilmRowMapper;
import ru.yandex.practicum.filmorate.util.MPARowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MPADbStorageImpl implements MPADbStorage{

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public MPADbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPA create(MPA mpa) {
        String sqlQuery = "insert into MPA(name, description) " +
                "values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery);
            ps.setString(1, mpa.getName());
            ps.setString(2, mpa.getDescription());
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            mpa.setId(keyHolder.getKey().longValue());
            return mpa;
        }
        return null;
    }

    @Override
    public MPA update(MPA mpa) throws Exception {
        String updateStatement = "UPDATE MPA SET ";
        String condition = "WHERE id=?";
        if(mpa.getId() == null) {
            throw new FilmNotFoundException("MPA not found");
        }

        if (mpa.getName() != null) {
            updateStatement += "mpa_name=?";
        }
        if (mpa.getDescription() != null) {
            updateStatement += "description=?";
        }
        updateStatement += condition;
        String finalUpdateStatement = updateStatement;
        int update = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(finalUpdateStatement);
            ps.setString(1, mpa.getName());
            ps.setString(2, mpa.getDescription());
            return ps;
        });
        if (update == 0) {
            throw new RuntimeException();

        }

        return null;
    }

    @Override
    public MPA delete(Long id) throws Exception {
        Optional<MPA> mpa = getById(id);
        if (mpa.isEmpty()) {
            throw new RuntimeException();
        }
        String deleteStatement = "DELETE FROM mpa WHERE id=?";
        jdbcTemplate.update(deleteStatement, id);
        return mpa.get();
    }

    @Override
    public Collection<MPA> getAll() {
        String selectStatement = "SELECT name, description FROM MPA";
        List<MPA> mpas = jdbcTemplate.queryForList(selectStatement, MPA.class);
        return mpas;
    }

    @Override
    public Optional<MPA> getById(Long id) throws Exception {
        String selectStatement = "SELECT name, description FROM MPA WHERE id=?";
        MPA mpa = jdbcTemplate.queryForObject(selectStatement, new Object[]{id}, new MPARowMapper());
        if (mpa == null) {
            return Optional.empty();
        }
        return Optional.of(mpa);
    }
}
