package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.AbstractModel;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, AbstractModel> storage = new HashMap<>();
    private static long counter = 1L;


    @Override
    public AbstractModel create(AbstractModel user) {
        user.setId(counter++);
        storage.put(user.getId(), user);
        return user;
    }

    @Override
    public AbstractModel update(AbstractModel user) throws Exception {
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
    public AbstractModel delete(Long id) throws Exception {
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
    public Collection<AbstractModel> getAll() {
        return storage.values();
    }

    @Override
    public AbstractModel getById(Long id) throws Exception {
        if (storage.containsKey(id)){
            return storage.get(id);
        }
        else {
            throw new FilmNotFoundException("Пользователь с id " + id +" не найден");
        }
    }

    public void clearStorage(){
        storage.clear();
    }
}
