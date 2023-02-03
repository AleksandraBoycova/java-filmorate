package ru.yandex.practicum.filmorate.service;

import de.svenjacobs.loremipsum.LoremIpsum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.yandex.practicum.filmorate.AbstractTest;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryFilmStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@AutoConfigureTestDatabase
@WebMvcTest({FilmStorage.class, FilmService.class})
class FilmServiceTest extends AbstractTest {

    @Autowired
    private FilmService         filmService;
    @MockBean(name = "dbFilmStorage")
    private FilmStorage storage;


    @Test
    void createFilm() throws ValidationException {
        Film film = buildFilm("Название фильма", "Описание фильма", "11.12.2020", 120);
        film.setId(12L);
        when(storage.create(any())).thenReturn(film);
        Film createdFilm = filmService.create(film);
        assertNotNull(createdFilm.getId());
        assertEquals(12L, createdFilm.getId());
    }

    @ParameterizedTest
    @MethodSource("prepareDataForCreateFilm")
    void createFilmWithValidationError(Film film, String expectedErrorMessage) {
        assertThrows(ValidationException.class, () -> filmService.create(film), expectedErrorMessage);
        verify(storage, times(0)).create(any());
    }

    @ParameterizedTest
    @MethodSource("prepareDataForCreateFilm")
    void updateFilmWithValidationError(Film film, String expectedErrorMessage) throws Exception {
        assertThrows(ValidationException.class, () -> filmService.create(film), expectedErrorMessage);
        verify(storage, times(0)).update(any());
    }


    @Test
    void updateFilm() throws Exception {
        Film updatedFilm = buildFilm("Название фильма", "новое описание", "11.12.2020", 120);
        when(storage.update(any())).thenReturn(updatedFilm);

        Film film = buildFilm("Название фильма", "Описание фильма", "11.12.2020", 120);
        film.setDescription("новое описание");

        Film updateFilm = filmService.update(film);
        assertEquals("новое описание", updateFilm.getDescription());
    }

    @Test
    void getAllFilms() {
        when(storage.getAll()).thenReturn(buildFilmList());

        Collection<Film> allFilms = filmService.getAll();
        assertEquals(6, allFilms.size());

    }

    @Test
    void likeFilm() throws Exception {
        Film film = buildFilm("Название фильма", "Описание фильма", "11.12.2020", 120);
        film.setId(12L);
        film.getLikes().add(11L);
        when(storage.getById(any())).thenReturn(Optional.of(film));
        when(storage.update(any())).thenReturn(film);

        filmService.likeFilm(12L, 11L);

        verify(storage, times(1)).getById(12L);
        verify(storage, times(1)).update(any());
        assertEquals(1, film.getLikes().size());
    }

    @Test
    void likeFilmWithError() throws Exception {
        when(storage.getById(any())).thenThrow(FilmNotFoundException.class);

        assertThrows(FilmNotFoundException.class, () -> filmService.likeFilm(2L, 11L));

        verify(storage, times(1)).getById(2L);
        verify(storage, times(0)).update(any());
    }

    @Test
    void dislikeFilm() throws Exception {
        Film film = buildFilm("Название фильма", "Описание фильма", "11.12.2020", 120);
        film.setId(12L);
        film.getLikes().add(11L);
        when(storage.getById(any())).thenReturn(Optional.of(film));
        Film film1 = buildFilm("Название фильма", "Описание фильма", "11.12.2020", 120);
        film1.setId(12L);
        when(storage.update(any())).thenReturn(film1);

        filmService.dislikeFilm(12L, 11L);

        verify(storage, times(1)).getById(12L);
        verify(storage, times(1)).update(any());
        assertTrue(film.getLikes().isEmpty());
    }

    @Test
    void dislikeFilmWithError() throws Exception {
        Film film = buildFilm("Название фильма", "Описание фильма", "11.12.2020", 120);
        film.setId(12L);
        film.getLikes().add(11L);
        when(storage.getById(any())).thenReturn(Optional.of(film));

        assertThrows(UserNotFoundException.class, () -> filmService.dislikeFilm(12L, 1L), "Пользователь 1 не ставил лайк к фильму 12");

        verify(storage, times(1)).getById(12L);
        verify(storage, times(0)).update(any());
    }

    @Test
    void getMostPopularFilms() {
        when(storage.getAll()).thenReturn(buildFilmList());

        List<Film> mostPopularFilms = filmService.getMostPopularFilms(3);
        assertEquals(3, mostPopularFilms.size());
        assertTrue(List.of("Иван царевич и серый волк", "Морозко", "Крокодил Гена", "Красная Шапочка").containsAll(mostPopularFilms.stream().map(Film::getName).collect(Collectors.toList())));

    }

    public static Stream<Arguments> prepareDataForCreateFilm() {
        return Stream.of(
                Arguments.of(buildFilm("", "Описание фильма", "11.12.2020", 120), "Название фильма не может быть пустым"),
                Arguments.of(buildFilm("Название фильма", "Описание фильма", "11.12.1894", 120), "дата релиза — не раньше 28.12.1895"),
                Arguments.of(buildFilm("Название фильма", "Описание фильма", "11.12.1894", -10), "продолжительность фильма должна быть положительной"),
                Arguments.of(buildFilm("Название фильма", new LoremIpsum().getWords(201), "11.12.1894", 120), "максимальная длина описания — 200 символов")
        );
    }

    private List<Film> buildFilmList() {
        Film f1 = buildFilm("Морозко", "Сказка", "01.12.1980", 90);
        f1.setLikes(Set.of(1L, 2L, 3L, 4L, 5L));
        Film f2 = buildFilm("Снегурочка", "Сказка", "01.12.1981", 60);
        f2.setLikes(Set.of(1L, 2L, 5L));
        Film f3 = buildFilm("Иван царевич и серый волк", "Сказка", "01.12.1985", 110);
        f3.setLikes(Set.of(1L, 2L, 3L, 4L, 5L, 6L));
        Film f4 = buildFilm("Чебурашка", "Сказка", "01.12.1983", 30);
        f4.setLikes(Set.of(1L, 2L));
        Film f5 = buildFilm("Крокодил Гена", "Мультфильм", "01.12.1989", 30);
        f5.setLikes(Set.of(1L, 2L, 4L, 5L));
        Film f6 = buildFilm("Красная Шапочка", "Сказка", "01.12.1990", 115);
        f6.setLikes(Set.of(1L, 2L, 3L, 4L));
        return List.of(f1, f2, f3, f4, f5, f6);
    }


}