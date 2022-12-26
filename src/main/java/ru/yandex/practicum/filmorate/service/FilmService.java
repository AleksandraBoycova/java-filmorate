package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

   public Film createFilm(Film film) throws ValidationException {
        validate(film);
        return filmStorage.createFilm(film);
    }

   public Film updateFilm(Film film) throws Exception {
        validate(film);
        return filmStorage.updateFilm(film);
    }

   public Film deleteFilm(Long id) throws Exception {
        return filmStorage.deleteFilm(id);
    }

   public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

   public Film getFilm(Long id) throws Exception {
        return filmStorage.getFilm(id);
    }

    public void likeFilm (Long filmId, Long userId) throws Exception {
        Film filmFromStorage = filmStorage.getFilm(filmId);
        filmFromStorage.getLikes().add(userId);
        filmStorage.updateFilm(filmFromStorage);
    }

    public void dislikeFilm (Long filmId, Long userId) throws Exception {
        Film filmFromStorage = filmStorage.getFilm(filmId);
        if (filmFromStorage.getLikes().contains(userId)) {
            filmFromStorage.getLikes().remove(userId);
            filmStorage.updateFilm(filmFromStorage);
        }
        else {
            throw new ValidationException("Пользователь " + userId + " не ставил лайк к фильму " + filmId);
        }
    }

    public List <Film> getMostPopularFilms (Integer count) {
        return filmStorage.getAllFilms()
                .stream()
                .sorted(Comparator.comparingInt(o -> o.getLikes().size()))
                .skip(Math.max(0, filmStorage.getAllFilms().size() - (count == null ? 10 : count)))
                .collect(Collectors.toList());

    }

    private void validate(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate
                .parse("28.12.1895", DateTimeFormatter.ofPattern("dd.MM.yyyy")))) {
            throw new ValidationException("дата релиза — не раньше 28.12.1895");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}
