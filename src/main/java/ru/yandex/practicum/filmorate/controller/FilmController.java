package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractModel;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("films")
@Slf4j
public class FilmController extends AbstractController {
    private FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        super(service);
        this.service = service;
    }

    @PostMapping
    public AbstractModel createFilm(@Valid @RequestBody Film film)
            throws ValidationException {
        return super.create(film);
    }

    @PutMapping
    public AbstractModel updateFilm(@Valid @RequestBody Film film)
            throws Exception {
        return super.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilm(@PathVariable Long id, @PathVariable Long userId) throws Exception {
        service.likeFilm(id, userId);
        log.info("Пользователь {} поставил лайк к фильму {}", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void dislikeFilm(@PathVariable Long id, @PathVariable Long userId) throws Exception {
        service.dislikeFilm(id, userId);
        log.info("Пользователь {} удалил лайк к фильму {}", userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", required = false) Integer count) {
        return service.getMostPopularFilms(count);
    }

}
