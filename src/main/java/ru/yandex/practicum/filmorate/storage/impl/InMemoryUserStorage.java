package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component ("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> storage = new HashMap<>();
    private static long counter = 1L;


    @Override
    public User create(User user) {
        user.setId(counter++);
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User user) throws Exception {
        Long id = user.getId();
        if (storage.containsKey(id)){
            storage.put(id, user);
        }
        else {
            throw new FilmNotFoundException("Пользователь с id " + id +" не найден");
        }
        return user;
    }

    @Override
    public User delete(Long id) throws Exception {
        if (storage.containsKey(id)){
            User user = (User) storage.get(id);
            storage.remove(id);
            return user;
        }
        else {
            throw new FilmNotFoundException("Пользователь с id " + id +" не найден");
        }
    }

    @Override
    public Collection<User> getAll() {
        return storage.values();
    }

    @Override
    public Optional<User> getById(Long id) throws Exception {
    return Optional.of(storage.get(id));
    }


    public void clearStorage(){
        storage.clear();
    }
}
