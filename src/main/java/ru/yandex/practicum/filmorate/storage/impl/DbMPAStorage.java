package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.util.MPARowMapper;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component
public class DbMPAStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public DbMPAStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPA create(MPA mpa) {
        String sqlQuery = "insert into MPA(mpa_name, description) " +
                "values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
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
        if(mpa.getId() == null) {
            throw new NotFoundException("MPA not found");
        }
        String updateStatement = "UPDATE MPA SET ";
        String condition = "WHERE mpa_id=?";
        List<Object> args = new ArrayList<>();

        if (mpa.getName() != null) {
            updateStatement += "mpa_name=?";
            args.add(mpa.getName());
        }
        if (mpa.getDescription() != null) {
            updateStatement += ", description=?";
            args.add(mpa.getDescription());
        }
        updateStatement += condition;
        args.add(mpa.getId());
        String finalUpdateStatement = updateStatement;
        jdbcTemplate.update(updateStatement, args.toArray());
        return getById(mpa.getId()).orElse(null);
    }

    @Override
    public MPA delete(Long id) throws Exception {
        Optional<MPA> mpa = getById(id);
        if (mpa.isEmpty()) {
            throw new NotFoundException("MPA no found");
        }
        String deleteStatement = "DELETE FROM mpa WHERE mpa_id=?";
        jdbcTemplate.update(deleteStatement, id);
        String s = "Update films set null where mpa_id = ?";
        jdbcTemplate.update(deleteStatement, id);
        return mpa.get();
    }

    @Override
    public Collection<MPA> getAll() {
        String selectStatement = "SELECT * FROM MPA";
        return jdbcTemplate.query(selectStatement, new MPARowMapper());
    }

    @Override
    public Optional<MPA> getById(Long id) throws Exception {
        String selectStatement = "SELECT * FROM MPA WHERE mpa_id=?";
        MPA mpa = jdbcTemplate.queryForObject(selectStatement, new MPARowMapper(), id);
        if (mpa == null) {
            return Optional.empty();
        }
        return Optional.of(mpa);
    }
}
