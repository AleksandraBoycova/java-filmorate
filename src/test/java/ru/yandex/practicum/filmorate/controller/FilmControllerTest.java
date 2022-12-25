package ru.yandex.practicum.filmorate.controller;

import de.svenjacobs.loremipsum.LoremIpsum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;

    public static Stream<Arguments> prepareDataForCreateFilm() {
        return Stream.of(
                Arguments.of(buildFilm("", "Описание фильма","11.12.2020", 120), "Название фильма не может быть пустым"),
                Arguments.of(buildFilm("Название фильма", "Описание фильма", "11.12.1894", 120), "дата релиза — не раньше 28.12.1895"),
                Arguments.of(buildFilm("Название фильма", "Описание фильма", "11.12.1894", -10),"продолжительность фильма должна быть положительной"),
                Arguments.of(buildFilm("Название фильма", new LoremIpsum().getWords(201), "11.12.1894", 120), "максимальная длина описания — 200 символов")
        );
    }

    @BeforeEach
    public void setUp () {
        filmController = new FilmController();
    }

    @Test
    void createFilm() throws ValidationException {
        Film film = buildFilm("Название фильма", "Описание фильма","11.12.2020", 120);
        Film createdFilm = filmController.createFilm(film);
        assertNotNull(createdFilm.getId());
    }
    @ParameterizedTest
    @MethodSource ("prepareDataForCreateFilm")
    void createFilmWithValidationError (Film film,String expectedErrorMessage) {
        assertThrows(ValidationException.class, ()-> filmController.createFilm(film), expectedErrorMessage);
    }


    @Test
    void updateFilm() throws ValidationException {
        Film film = buildFilm("Название фильма", "Описание фильма","11.12.2020", 120);
        Film createdFilm = filmController.createFilm(film);
        createdFilm.setDescription("новое описание");
        Film updateFilm = filmController.updateFilm(createdFilm);
        assertEquals("новое описание", updateFilm.getDescription());
    }

    @Test
    void getAllFilms() throws ValidationException {
        Film film = buildFilm("Название фильма", "Описание фильма","11.12.2020", 120);
        Film createdFilm = filmController.createFilm(film);
        Film film1 = buildFilm("Название фильма один", "Описание фильма новое","10.12.2020", 120);
        Film createdFilm1 = filmController.createFilm(film1);
        Collection<Film> allFilms = filmController.getAll();
        assertEquals(2, allFilms.size());

    }

    private static Film buildFilm (String name, String description, String dateString, int duration) {
        Film film = new Film();
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(LocalDate
                .parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        film.setDuration(duration);
        return film;

    }
}