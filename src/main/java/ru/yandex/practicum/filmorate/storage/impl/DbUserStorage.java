package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.FriendshipStatus;
import ru.yandex.practicum.filmorate.util.UserRowMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Component ("dbUserStorage")
public class DbUserStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        String sqlQuery = "insert into \"user\"(email, login, user_name, birthday) " +
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
            user.getFriends().forEach(friendId -> updateFriendship(user.getId(), friendId));
            return user;
        }
        return null;
    }

    @Override
    public User update(User user) throws Exception {
        if (user.getId() == null) {
            throw new NotFoundException("User not found");
        }
        String updateStatement = "UPDATE \"user\" SET ";
        String condition       = "WHERE user_id=?";
        String delimiter       = "";

        List<Object> args = new ArrayList<>();
        if (user.getName() != null) {
            updateStatement += "user_name=?";
            args.add(user.getName());
        }
        delimiter = ", ";
        if (user.getLogin() != null) {
            updateStatement += delimiter + "login=?";
            args.add(user.getLogin());
        }
        if (user.getEmail() != null) {
            updateStatement += delimiter + "email=?";
            args.add(user.getEmail());
        }
        if (user.getBirthday() != null) {
            updateStatement += delimiter + "birthday=?";
            args.add(Date.valueOf(user.getBirthday()));
        }
        updateStatement += condition;
        args.add(user.getId());
        String finalUpdateStatement = updateStatement;
        jdbcTemplate.update(finalUpdateStatement, args.toArray());
        user.getFriends().forEach(friendId -> updateFriendship(user.getId(), friendId));

        return getById(user.getId()).orElse(null);
    }

    @Override
    public User delete(Long id) throws Exception {
        Optional<User> u = getById(id);
        if (u.isEmpty()) {
            throw new NotFoundException("User not found");
        }
        String deleteStatement = "DELETE FROM \"user\" WHERE user_id=?";
        jdbcTemplate.update(deleteStatement, id);
        return u.get();
    }

    @Override
    public Collection<User> getAll() {
        String selectStatement = "SELECT * FROM \"user\"";
        return jdbcTemplate.query(selectStatement,  new UserRowMapper());
    }

    @Override
    public Optional<User> getById(Long id) throws Exception {
        String selectStatement = "SELECT * FROM \"user\" WHERE user_id=?";
        User user = jdbcTemplate.queryForObject(selectStatement, new UserRowMapper(), id);
        if (user == null) {
            return Optional.empty();
        }
        user.setFriends(new HashSet<>(jdbcTemplate.queryForList("SELECT friend_id FROM friendship WHERE user_id=?", Long.class, id)));
        return Optional.of(user);
    }

    private void updateFriendship(Long userId, Long friendId) {
        String createFriendshipEntry = "INSERT INTO friendship (user_id, friend_id, friendship_status) VALUES (?, ?, ?)";
        jdbcTemplate.update(createFriendshipEntry, userId, friendId, FriendshipStatus.NOT_ACCEPTED.name());
        String updateStatement = "UPDATE friendship SET friendship_status=? WHERE (user_id=? AND friend_id=? AND EXISTS (SELECT 1 FROM friendship WHERE user_id=? and friend_id=?);" +
                "UPDATE friendship SET friendship_status=? WHERE user_id=? AND friend_id=? AND (SELECT friendship_status FROM friendship WHERE user_id=? AND friend_id=?) = ?;";
        jdbcTemplate.update(updateStatement, FriendshipStatus.ACCEPTED.name(), userId, friendId, friendId, userId,
                FriendshipStatus.ACCEPTED.name(), friendId, userId, userId, friendId, FriendshipStatus.ACCEPTED.name());
    }
}
