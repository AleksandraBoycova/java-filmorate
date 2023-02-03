package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component ("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> storage = new HashMap<>();
    private static long                    counter = 1L;

    @Override
    public Film create(Film film) {
        film.setId(counter++);
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) throws Exception {
        Long id = film.getId();
        if (storage.containsKey(id)){
            storage.put(id, film);
        }
        else {
            throw new NotFoundException("Фильм с id " + id +" не найден");
        }
        return film;
    }

    @Override
    public Film delete(Long id) throws Exception {
        if (storage.containsKey(id)){
            Film film = (Film) storage.get(id);
            storage.remove(id);
            return film;
        }
        else {
            throw new NotFoundException("Фильм с id " + id +" не найден");
        }
    }

    @Override
    public Collection<Film> getAll() {
        return storage.values();
    }

    @Override
    public Optional<Film> getById(Long id) throws Exception {
        Film filmFromStorage = storage.get(id);
        if (filmFromStorage != null) {
            return Optional.of(filmFromStorage);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public boolean isExists(Long id) {
        return storage.containsKey(id);
    }

    public void clearStorage(){
        storage.clear();
    }

}
