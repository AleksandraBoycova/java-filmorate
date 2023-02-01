package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.DbGenreStorage;
import ru.yandex.practicum.filmorate.storage.impl.DbMPAStorage;
import ru.yandex.practicum.filmorate.storage.impl.DbUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Profile("test")
public class FilmorateApplicationTests {
    private final DbUserStorage userStorage;
    private final DbFilmStorage filmStorage;
    private final DbGenreStorage genreStorage;
    private final DbMPAStorage mpaStorage;

    @Test
    void contextLoads() { }

    @Test
    public void testFindUserById() throws Exception {

        Optional<User> userOptional = userStorage.getById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testCreateUser() {
        User user = buildUser(1L, "login", "name", "email@mail.com", LocalDate.now());
        User createdUser = userStorage.create(user);
       assertNotNull(createdUser.getId());
    }

    @Test
    public void testUpdateUser() throws Exception {
        User user = buildUser(1L, "login", "new name", "email@mail.com", LocalDate.now());
        User updatedUser = userStorage.update(user);
        assertEquals(1L, updatedUser.getId());
        assertEquals("new name", updatedUser.getName());
    }

    private User buildUser(Long id, String login, String name, String email, LocalDate birthday) {
        User user = new User();
        user.setId(id);
        user.setLogin(login);
        user.setName(name);
        user.setEmail(email);
        user.setBirthday(birthday);
        return user;
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(buildUser(2L,"login2", "name2", "email2@mail.com", LocalDate.now()));
        users.add(buildUser(3L,"login3", "name3", "email3@mail.com", LocalDate.now()));
        users.add(buildUser(4L,"login4", "name4", "email4@mail.com", LocalDate.now()));
        users.add(buildUser(5L,"login5", "name5", "email5@mail.com", LocalDate.now()));
        Collection<User> all = userStorage.getAll();
        assertTrue(all.containsAll(users));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User deletedUser = userStorage.delete(1L);
        assertFalse(userStorage.getAll().contains(deletedUser));
    }
    @Test
    public void testCreateFilm() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        film.setGenre(new Genre(1L, "comedy"));
        //film.setMpa(new MPA(1L,"G", "у фильма нет возрастных ограничений"));
    }

    @Test
    public void testUpdateFilm() throws Exception {
        Film film = buildFilm(1L, "new name", "description", LocalDate.now(), 120, 1, 1);
        Film updatedFilm = filmStorage.update(film);
        assertEquals(1L, updatedFilm.getId());
        assertEquals("new name", updatedFilm.getName());
    }

    private Film buildFilm(Long id, String name, String description, LocalDate releaseDate, int duration, int genre, int mpa) {
        Film film = new Film();
        film.setId(id);
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);
        film.setGenre(new Genre(1L,"comedy"));
        //film.setMpa(new MPA(1L,"G", "у фильма нет возрастных ограничений"));
        return film;
    }

    @Test
    public void testGetAllFilms() {
        List<Film> films = new ArrayList<>();
        films.add(buildFilm(2L, "name2", "description2", LocalDate.now(), 120, 1, 1));
        films.add(buildFilm(3L, "name3", "description3", LocalDate.now(), 120, 1, 1));
        films.add(buildFilm(4L, "name4", "description4", LocalDate.now(), 120, 1, 1));
        films.add(buildFilm(5L, "name5", "description5", LocalDate.now(), 120, 1, 1));
        Collection<Film> all = filmStorage.getAll();
        assertTrue(all.containsAll(films));
    }

    @Test
    public void testDeleteFilm() throws Exception {
        Film deletedFilm = filmStorage.delete(1L);
        assertFalse(filmStorage.getAll().contains(deletedFilm));
    }

    @Test
    public void testCreateGenre() {
        Genre genre = new Genre();
        genre.setName("name");
    }

    @Test
    public void testUpdateGenre() throws Exception {
        Genre genre = buildGenre(1L, "new name");
        Genre updatedGenre = genreStorage.update(genre);
        assertEquals(1L, updatedGenre.getId());
        assertEquals("new name", updatedGenre.getName());
    }

    private Genre buildGenre(Long id, String name) {
        Genre genre = new Genre();
        genre.setId(id);
        genre.setName(name);
        return genre;
    }

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = new ArrayList<>();
        genres.add(buildGenre(2L, "drama"));
        genres.add(buildGenre(3L, "cartoon"));
        genres.add(buildGenre(4L, "thriller"));
        genres.add(buildGenre(5L, "documentary"));
        Collection<Genre> all = genreStorage.getAll();
        assertTrue(all.containsAll(genres));
    }

    @Test
    public void testDeleteGenre() throws Exception {
        Genre deletedGenre = genreStorage.delete(1L);
        assertFalse(genreStorage.getAll().contains(deletedGenre));
    }

    @Test
    public void testCreateMPA() {
        MPA mpa = new MPA();
        mpa.setName("name");
        mpa.setDescription("description");
    }

    @Test
    public void testUpdateMPA() throws Exception {
        MPA mpa = buildMPA(1L, "new name", "description");
        MPA updatedMPA = mpaStorage.update(mpa);
        assertEquals(1L, updatedMPA.getId());
        assertEquals("new name", updatedMPA.getName());
    }

    private MPA buildMPA(Long id, String name, String description) {
        MPA mpa = new MPA();
        mpa.setId(id);
        mpa.setName(name);
        mpa.setDescription("description");
        return mpa;
    }

    @Test
    public void testGetAllMPA() {
        List<MPA> mpas = new ArrayList<>();
        mpas.add(buildMPA(2L, "PG", "детям рекомендуется смотреть фильм с родителями"));
        mpas.add(buildMPA(3L, "PG_13", "детям до 13 лет просмотр не желателен"));
        mpas.add(buildMPA(4L, "R", "лицам до 17 лет просматривать фильм можно только в присутствии взрослого"));
        Collection<MPA> all = mpaStorage.getAll();
        assertTrue(all.containsAll(mpas));
    }

    @Test
    public void testDeleteMPA() throws Exception {
        MPA deletedMPAs = mpaStorage.delete(1L);
        assertFalse(mpaStorage.getAll().contains(deletedMPAs));
    }
}
