package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.util.Collection;

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
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        int update = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(sqlQuery);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setString(4, String.valueOf(user.getBirthday()));
            return ps;
        });
        return null;
    }

    @Override
    public User update(User user) throws Exception {
        return null;
    }

    @Override
    public User delete(Long id) throws Exception {
        return null;
    }

    @Override
    public Collection<User> getAll() {
        return null;
    }

    @Override
    public User getById(Long id) throws Exception {
        return null;
    }
}
