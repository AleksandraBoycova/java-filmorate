package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;
import java.util.Optional;

@Service
public class GenreService extends AbstractService <Genre> {
    private GenreStorage genreStorage;

    public GenreService(Storage<Genre> storage) {
        super(storage);
    }

    @Override
    protected void validate(Genre model) throws ValidationException {

    }

    public Genre create(Genre genre) throws ValidationException {
        validate(genre);
        return storage.create(genre);
    }

    public Genre update(Genre genre) throws Exception {
        validate(genre);
        return storage.update(genre);
    }

    public Genre delete(Long id) throws Exception {
        return storage.delete(id);
    }

    public Collection<Genre> getAll() {
        return storage.getAll();
    }

    public Genre getById(Long id) throws Exception {
        Optional<Genre> optionalGenre = storage.getById(id);
        if (optionalGenre.isEmpty()) {
            throw new RuntimeException();
        }
        return optionalGenre.get();
    }
}
