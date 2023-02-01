package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmService extends AbstractService <Film>{
   private final DateTimeFormatter formatter;

    @Autowired
    public FilmService(@Qualifier("dbFilmStorage") FilmStorage storage) {
        super(storage);
        formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    }

    public void likeFilm(Long filmId, Long userId) throws Exception {
        Optional<Film> filmFromStorage = storage.getById(filmId);
        if (filmFromStorage.isEmpty()) {
            throw new FilmNotFoundException("Film not found");
        }
        filmFromStorage.get().getLikes().add(userId);
        storage.update(filmFromStorage.get());
    }

    public void dislikeFilm(Long filmId, Long userId) throws Exception {
        Optional<Film> filmFromStorage = storage.getById(filmId);
        if (filmFromStorage.isEmpty()) {
            throw new FilmNotFoundException("Film not found");
        }
        if (filmFromStorage.get().getLikes().contains(userId)) {
            filmFromStorage.get().getLikes().remove(userId);
            storage.update(filmFromStorage.get());
        } else {
            throw new UserNotFoundException("Пользователь " + userId + " не ставил лайк к фильму " + filmId);
        }
    }

    public List<Film> getMostPopularFilms(Integer count) {
        return storage.getAll()
                .stream()
                .sorted(Comparator.comparingInt(o -> o.getLikes().size()))
                .skip(Math.max(0, storage.getAll().size() - (count == null ? 10 : count)))
                .collect(Collectors.toList());

    }

    @Override
    protected void validate(Film abstractModel) throws ValidationException {
        Film film = abstractModel;
        if (film.getName().isBlank()) {
            throw new ValidationException(film, "Название фильма не может быть пустым", "name", film.getName());
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException(film, "максимальная длина описания — 200 символов", "description", film.getDescription());
        }
        if (film.getReleaseDate().isBefore(LocalDate
                .parse("28.12.1895", formatter))) {
            throw new ValidationException(film, "дата релиза — не раньше 28.12.1895", "releaseDate", film.getReleaseDate().format(formatter));
        }
        if (film.getDuration() < 0) {
            throw new ValidationException(film, "продолжительность фильма должна быть положительной", "duration", film.getDuration());
        }
    }
}
