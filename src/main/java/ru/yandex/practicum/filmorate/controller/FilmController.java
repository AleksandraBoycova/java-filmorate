package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping ("films")
@Slf4j
public class FilmController extends AbstractController <Film>{

    @PostMapping
    public Film createFilm (@Valid @RequestBody Film film)
            throws ValidationException {
        validate(film);
        film.setId(counter++);
        storage.put(film.getId(), film);
        log.info("Создан фильм {}",film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm (@Valid @RequestBody Film film)
            throws ValidationException {
        log.info("Обновляем фильм {}", film.getId());
        validate(film);
        Long id = film.getId();
        if (storage.containsKey(id)){
            storage.put(id, film);
            log.info("Фильм {} отредактирован", film.getName());
        }
        else {
            throw new ValidationException("Фильм с id " + id +" не найден");
        }
        return film;
    }

    @Override
    public void validate (Film film) throws ValidationException {
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
        if (film.getDuration() < 0) {
            throw new ValidationException("продолжительность фильма должна быть положительной");
        }
    }
}
