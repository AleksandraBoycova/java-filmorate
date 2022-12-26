package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AbstractTest {
    public static Film buildFilm (String name, String description, String dateString, int duration) {
        Film film = new Film();
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(LocalDate
                .parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        film.setDuration(duration);
        return film;

    }

    public static User buildUser (String email, String login, String name, String dateString) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(LocalDate
                .parse(dateString, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        return user;
    }

}
