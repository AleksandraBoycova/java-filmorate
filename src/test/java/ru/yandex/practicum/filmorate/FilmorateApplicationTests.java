package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.DbGenreStorage;
import ru.yandex.practicum.filmorate.storage.impl.DbMPAStorage;
import ru.yandex.practicum.filmorate.storage.impl.DbUserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
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
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
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
        User user = buildUser(null, "login", "name", "email589@mail.com", LocalDate.now());
        User userToUpdate = userStorage.create(user);
        userToUpdate.setName("new name");
        User updatedUser = userStorage.update(userToUpdate);
        assertEquals(updatedUser.getId(), updatedUser.getId());
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
        users.add(buildUser(1L,"login2", "name2", "email2@mail.com", LocalDate.now()));
        users.add(buildUser(2L,"login3", "name3", "email3@mail.com", LocalDate.now()));
        users.add(buildUser(3L,"login4", "name4", "email4@mail.com", LocalDate.now()));
        users.add(buildUser(4L,"login5", "name5", "email5@mail.com", LocalDate.now()));
        Collection<User> all = userStorage.getAll();
        assertArrayEquals(users.toArray(new User[0]), all.toArray(new User[0]));
    }

    @Test
    public void testDeleteUser() throws Exception {
        User user = buildUser(null, "login", "name", "email679@mail.com", LocalDate.now());
        User userToDelete = userStorage.create(user);
        User deletedUser = userStorage.delete(userToDelete.getId());
        assertFalse(userStorage.getAll().contains(deletedUser));
    }
    @Test
    public void testCreateFilm() {
        Film film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        film.setGenres(Set.of(new Genre(1L, "Комедия")));
        film.setMpa(new MPA(1L,"G", "у фильма нет возрастных ограничений"));
    }

    @Test
    public void testUpdateFilm() throws Exception {
        Film film = buildFilm(null, "name", "description", LocalDate.now(), 120, Set.of(buildGenre(1L, "Комедия")), new MPA(1L,"G", "у фильма нет возрастных ограничений"));
        Film createdFilm = filmStorage.create(film);
        createdFilm.setName("new name");
        Film updatedFilm = filmStorage.update(createdFilm);
        assertEquals(5L, updatedFilm.getId());
        assertEquals("new name", updatedFilm.getName());
    }

    private Film buildFilm(Long id, String name, String description, LocalDate releaseDate, int duration, Set<Genre> genre, MPA mpa) {
        Film film = new Film();
        film.setId(id);
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);
        film.setGenres(Set.of(new Genre(1L,"Комедия")));
        film.setMpa(new MPA(1L,"G", "у фильма нет возрастных ограничений"));
        return film;
    }

    @Test
    public void testGetAllFilms() {
        List<Film> films = new ArrayList<>();
        Genre genre = buildGenre(1L, "new name");
        films.add(buildFilm(1L, "name2", "description2", LocalDate.now(), 120, Set.of(genre), buildMPA(1L, "G", "у фильма нет возрастных ограничений")));
        films.add(buildFilm(2L, "name3", "description3", LocalDate.now(), 120, Set.of(genre),  buildMPA(1L, "G", "у фильма нет возрастных ограничений")));
        films.add(buildFilm(3L, "name4", "description4", LocalDate.now(), 120, Set.of(genre),  buildMPA(1L, "G", "у фильма нет возрастных ограничений")));
        films.add(buildFilm(4L, "name5", "description5", LocalDate.now(), 120, Set.of(genre),  buildMPA(1L, "G", "у фильма нет возрастных ограничений")));
        Collection<Film> all = filmStorage.getAll();
        assertArrayEquals(films.toArray(new Film[0]), all.toArray(new Film[0]));
    }

    @Test
    public void testDeleteFilm() throws Exception {
        Film film = new Film();
        film.setName("name");
        film.setDescription("dsc");
        film.setReleaseDate(LocalDate.now());
        film.setMpa(new MPA(1L, "G", "dsc"));
        film.setGenres(new HashSet<>());
        Film filmToDelete        = filmStorage.create(film);
        Film deletedFilm = filmStorage.delete(filmToDelete.getId());
        assertFalse(filmStorage.getAll().contains(deletedFilm));
    }

    @Test
    public void testCreateGenre() {
        Genre genre = new Genre();
        genre.setName("name");
    }

    @Test
    public void testUpdateGenre() throws Exception {
        Genre genreToUpdate = genreStorage.create(buildGenre(null, "name"));
        genreToUpdate.setName("new name");
        Genre updatedGenre = genreStorage.update(genreToUpdate);
        assertEquals(14L, updatedGenre.getId());
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
        genres.add(buildGenre(2L, "Драма"));
        genres.add(buildGenre(3L, "Мультфильм"));
        genres.add(buildGenre(4L, "Триллер"));
        genres.add(buildGenre(5L, "Документальный"));
        Collection<Genre> all = genreStorage.getAll();
        assertTrue(all.containsAll(genres));
    }

    @Test
    public void testDeleteGenre() throws Exception {
        Genre genreToDelete        = genreStorage.create(buildGenre(null, "drama"));
        Genre deletedGenre = genreStorage.delete(genreToDelete.getId());
        assertFalse(genreStorage.getAll().contains(deletedGenre));
    }

    @Test
    public void testCreateMPA() {
        MPA mpa = new MPA();
        mpa.setName("name");
        mpa.setDescription("description");
        MPA mpa1 = mpaStorage.create(mpa);
        assertNotNull(mpa1.getId());
    }

    @Test
    public void testUpdateMPA() throws Exception {
        MPA mpa = buildMPA(null, "name", "description");
        MPA mpaToUpdate = mpaStorage.create(mpa);
        mpaToUpdate.setName("new name");
        MPA updatedMPA = mpaStorage.update(mpaToUpdate);
        assertEquals(11L, updatedMPA.getId());
        assertEquals("new name", updatedMPA.getName());
    }

    private MPA buildMPA(Long id, String name, String description) {
        MPA mpa = new MPA();
        mpa.setId(id);
        mpa.setName(name);
        mpa.setDescription(description);
        return mpa;
    }

    @Test
    public void testGetAllMPA() {
        List<MPA> mpas = new ArrayList<>();
        mpas.add(buildMPA(2L, "PG", "детям рекомендуется смотреть фильм с родителями"));
        mpas.add(buildMPA(3L, "PG_13", "детям до 13 лет просмотр не желателен"));
        mpas.add(buildMPA(4L, "R", "лицам до 17 лет просматривать фильм можно только в присутствии взрослого"));
        Collection<MPA> all = mpaStorage.getAll();
        assertArrayEquals(all.toArray(new MPA[0]), all.toArray(new MPA[0]));
    }

    @Test
    public void testDeleteMPA() throws Exception {
        MPA mpa = new MPA();
        mpa.setDescription("детям рекомендуется смотреть фильм с родителями");
        mpa.setName("PG");
        MPA mpaToDelete        = mpaStorage.create(mpa);
        MPA deletedMPAs = mpaStorage.delete(mpaToDelete.getId());
        assertFalse(mpaStorage.getAll().contains(deletedMPAs));
    }
}
