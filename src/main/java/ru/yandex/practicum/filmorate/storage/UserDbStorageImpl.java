package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;

public class UserDbStorageImpl implements UserDbStorage {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public UserDbStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into users(email, login, name, birthday) " +
                "values (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery);
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
        String updateStatement = "UPDATE \"user\" SET ";//login=?, email=?, birthday=?
        String condition = "WHERE id=?";
        if(user.getId() == null) {
            throw new UserNotFoundException("User not found");
        }
        if (user.getName() != null) {
            updateStatement += "user_name=?";
        }
        if (user.getLogin() != null) {
            updateStatement += "login=?";
        }
        if (user.getEmail() != null) {
            updateStatement += "email=?";
        }
        if (user.getBirthday() != null) {
            updateStatement += "birthday=?";
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
        User u = getById(id);
        String deleteStatement = "DELETE FROM \"user\" WHERE id=?";
        jdbcTemplate.update(deleteStatement, id);
        return u;
    }

    @Override
    public Collection<User> getAll() {
        String selectStatement = "SELECT id, user_name, login, email, birthday FROM \"user\"";
        List<User> users = jdbcTemplate.queryForList(selectStatement, User.class);
        return users;
    }

    @Override
    public User getById(Long id) throws Exception {
        String selectStatement = "SELECT id, user_name, login, email, birthday FROM \"user\" WHERE id=?";
        User user = jdbcTemplate.queryForObject(selectStatement, new Object[]{id}, new UserRowMapper());
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        return user;
    }
}
