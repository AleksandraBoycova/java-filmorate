package ru.yandex.practicum.filmorate.storage;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryFilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(InMemoryFilmStorage.class)
class InMemoryFilmStorageTest {

    @Autowired
    private InMemoryFilmStorage storage;

    private Film currentFilm;

    @BeforeEach
    void setUp() {
        Film film = buildFilm("Морозко", "Сказка", "01.12.1980", 90);
        currentFilm = storage.create(film);
        storage.create(buildFilm("Снегурочка", "Сказка", "01.12.1981", 60));
        storage.create(buildFilm("Иван царевич и серый волк", "Сказка", "01.12.1985", 110));
        storage.create(buildFilm("Чебурашка", "Сказка", "01.12.1983", 30));
    }

    @AfterEach
    void tearDown() {
        storage.clearStorage();
    }

    @Test
    void deleteFilm() throws Exception {
        Film deletedFilm = storage.delete(currentFilm.getId());
        assertThrows(FilmNotFoundException.class, () -> storage.getById(deletedFilm.getId()), "Фильм с id " + deletedFilm.getId() + " не найден");
    }

    @Test
    void deleteFilmWithError() {
        assertThrows(FilmNotFoundException.class, () -> storage.delete(Long.MAX_VALUE), "Фильм с id " + Long.MAX_VALUE + " не найден");
    }

    @Test
    void getFilm() throws Exception {
        Optional<Film> filmFromStorage = storage.getById(currentFilm.getId());
        assertTrue(filmFromStorage.isPresent());
        assertEquals("Морозко", filmFromStorage.get().getName());
        assertEquals("Сказка", filmFromStorage.get().getDescription());
        assertEquals(getLocalDateFromString("01.12.1980"), filmFromStorage.get().getReleaseDate());
        assertEquals(90, filmFromStorage.get().getDuration());
    }

    @Test
    void getFilmWithError() {
        assertThrows(FilmNotFoundException.class, () -> storage.getById(Long.MAX_VALUE), "Фильм с id " + Long.MAX_VALUE + " не найден");
    }

    @Test
    void createFilm() {
        Film          film        = buildFilm("Название фильма", "Описание фильма", "11.12.2020", 120);
        Film createdFilm = storage.create(film);
        assertNotNull(createdFilm.getId());
    }

    @Test
    void updateFilm() throws Exception {
        currentFilm.setDescription("Новогодняя сказка");
        Long idBeforeUpdate = currentFilm.getId();
        Film updateFilm     = storage.update(currentFilm);

        assertEquals("Новогодняя сказка", updateFilm.getDescription());
        assertEquals(idBeforeUpdate, currentFilm.getId());
    }

    @Test
    void updateFilmWithError() {
        Film f = buildFilm("Снегурочка", "Сказка", "01.12.1981", 60);
        f.setId(Long.MAX_VALUE);

        assertThrows(FilmNotFoundException.class, () -> storage.update(f), "Фильм с id " + Long.MAX_VALUE + " не найден");
    }

    @Test
    void getAllFilms() {
        Collection<Film> allFilms = storage.getAll();
        assertEquals(4, allFilms.size());

    }

    private static Film buildFilm(String name, String description, String dateString, int duration) {
        Film film = new Film();
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(getLocalDateFromString(dateString));
        film.setDuration(duration);
        return film;

    }

    private static LocalDate getLocalDateFromString(String dateString) {
        return LocalDate
                .parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}