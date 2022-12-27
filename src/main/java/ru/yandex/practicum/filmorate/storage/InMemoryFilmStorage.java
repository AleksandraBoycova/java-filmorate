package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.AbstractModel;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, AbstractModel> storage = new HashMap<>();
    private static long                    counter = 1L;

    @Override
    public AbstractModel create(AbstractModel film) {
        film.setId(counter++);
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public AbstractModel update(AbstractModel film) throws Exception {
        Long id = film.getId();
        if (storage.containsKey(id)){
            storage.put(id, film);
        }
        else {
            throw new FilmNotFoundException("Фильм с id " + id +" не найден");
        }
        return film;
    }

    @Override
    public AbstractModel delete(Long id) throws Exception {
        if (storage.containsKey(id)){
            Film film = (Film) storage.get(id);
            storage.remove(id);
            return film;
        }
        else {
            throw new FilmNotFoundException("Фильм с id " + id +" не найден");
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
            throw new FilmNotFoundException("Фильм с id " + id +" не найден");
        }
    }

    public void clearStorage(){
        storage.clear();
    }

}
