package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film createFilm (Film film);
    Film updateFilm (Film film) throws Exception;
    Film deleteFilm (Long id) throws Exception;
    Collection <Film> getAllFilms ();
    Film getFilm (Long id) throws Exception;
}
