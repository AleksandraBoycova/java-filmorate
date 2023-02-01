package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.UserRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.Optional;

@Component
public class DbUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into users(email, login, user_name, birthday) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            user.setId(keyHolder.getKey().longValue());
            return user;
        }
        return null;
    }

    @Override
    public User update(User user) throws Exception {
        String updateStatement = "UPDATE \"user\" SET ";
        String condition = "WHERE id=?";
        String delimiter = "";
        if(user.getId() == null) {
            throw new UserNotFoundException("User not found");
        }
        if (user.getName() != null) {
            updateStatement += "user_name=?";
        }
        delimiter = ", ";
        if (user.getLogin() != null) {
            updateStatement += delimiter + "login=?";
        }
        if (user.getEmail() != null) {
            updateStatement += delimiter + "email=?";
        }
        if (user.getBirthday() != null) {
            updateStatement += delimiter + "birthday=?";
        }
        updateStatement += condition;
        String finalUpdateStatement = updateStatement;
        int update = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(finalUpdateStatement);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        });
        if (update == 0) {
            throw new RuntimeException();
        }
        return null;
    }

    @Override
    public User delete(Long id) throws Exception {
        Optional<User> u = getById(id);
        if (u.isEmpty()) {
            throw new UserNotFoundException("User not found");
        }
        String deleteStatement = "DELETE FROM \"user\" WHERE id=?";
        jdbcTemplate.update(deleteStatement, id);
        return u.get();
    }

    @Override
    public Collection<User> getAll() {
        String selectStatement = "SELECT id, user_name, login, email, birthday FROM \"user\"";
        return jdbcTemplate.queryForList(selectStatement, User.class);
    }

    @Override
    public Optional<User> getById(Long id) throws Exception {
        String selectStatement = "SELECT id, user_name, login, email, birthday FROM \"user\" WHERE id=?";
        User user = jdbcTemplate.queryForObject(selectStatement, new UserRowMapper(), id);
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(user);
    }
}
