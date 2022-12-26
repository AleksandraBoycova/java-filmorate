package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> storage = new HashMap<>();
    private static long counter = 1L;

    @Override
    public Film createFilm(Film film) {
        film.setId(counter++);
        storage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) throws Exception {
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
    public Film deleteFilm(Long id) throws Exception {
        if (storage.containsKey(id)){
            Film film = storage.get(id);
            storage.remove(id);
            return film;
        }
        else {
            throw new FilmNotFoundException("Фильм с id " + id +" не найден");
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        return storage.values();
    }

    @Override
    public Film getFilm(Long id) throws Exception {
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
