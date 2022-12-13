package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping ("films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> storage = new HashMap<>();
    private static long counter = 1L;

    @PostMapping
    public Film createFilm (@Valid @RequestBody Film film)
            throws ValidationException {
        validate(film);
        film.setId(counter++);
        storage.put(film.getId(), film);
        log.info("Создан фильм {}",film.getName());
        return film;
    }

    @PutMapping("/{id}")
    public Film updateFilm (@PathVariable long id,@Valid @RequestBody Film film)
            throws ValidationException {
        validate(film);
        if (storage.containsKey(id)){
            storage.put(id, film);
            log.info("Фильм {} отредактирован", film.getName());
        }
        else {
            log.error("Фильм с id {} не найден", id);
        }
        return film;
    }
    @GetMapping
    public Collection<Film> getAllFilms (){
        return storage.values();
    }

    private void validate (Film film) throws ValidationException {
        if (film.getName().isBlank()){
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate
                .parse("28.12.1895", DateTimeFormatter.ofPattern("dd.MM.yyyy")))){
            throw new ValidationException("дата релиза — не раньше 28.12.1895");
        }
        if (film.getDuration().isNegative()) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}
