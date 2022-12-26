package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User createUser (User user);
    User updateUser (User user) throws Exception;
    User deleteUser (Long id) throws Exception;
    Collection<User> getAllUsers ();
    User getUser (Long id) throws Exception;
}
