package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("films")
@Slf4j
public class FilmController extends AbstractController<Film> {
    private FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film)
            throws ValidationException {
        log.info("Создаем новый фильм ");
        Film createdFilm = service.createFilm(film);
        log.info("Создан фильм {}", film.getName());
        return createdFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film)
            throws Exception {
        log.info("Обновляем фильм {}", film.getId());
        Film updatedFilm = service.updateFilm(film);
        log.info("Фильм {} отредактирован", film.getName());
        return updatedFilm;
    }

    @DeleteMapping("{id}")
    public Film deleteFilm (@PathVariable Long id) throws Exception {
        log.info("Удаляем фильм {}", id);
        Film deleteFilm = service.deleteFilm(id);
        log.info("Фильм {} удален", deleteFilm.getName());
        return deleteFilm;
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void likeFilm (@PathVariable Long id, @PathVariable Long userId) throws Exception {
        service.likeFilm(id, userId);
        log.info("Пользователь {} поставил лайк к фильму {}", userId, id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void dislikeFilm (@PathVariable Long id, @PathVariable Long userId) throws Exception {
        service.dislikeFilm(id, userId);
        log.info("Пользователь {} удалил лайк к фильму {}", userId, id);
    }

    @GetMapping("/films/popular")
    public List <Film> getPopularFilms(@RequestParam (value = "count", required = false) Integer count) {
        return service.getMostPopularFilms(count);
    }


}
