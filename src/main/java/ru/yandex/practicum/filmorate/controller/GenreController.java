package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.AbstractService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;


@RestController
@Slf4j
@RequestMapping("genres")
public class GenreController extends AbstractController <Genre> {
    private GenreService genreService;

    public GenreController(AbstractService<Genre> service) {
        super(service);
    }

    @Autowired
    public GenreController(GenreService genreService) {
        super(genreService);
        this.genreService = genreService;
    }

    @PostMapping
    public Genre createGenre(@Valid @RequestBody Genre genre)
            throws ValidationException {
        return super.create(genre);
    }

    @PutMapping
    public Genre updateGenre(@Valid @RequestBody Genre genre)
            throws Exception {
        return super.update(genre);
    }

    @DeleteMapping ("/{id}/genres/{fgenreId}")
    public void deleteGenre (@PathVariable Long id, @PathVariable Long genreId) throws Exception {
        log.info("Удаляем жанр");
        genreService.delete(genreId);
    }

}
