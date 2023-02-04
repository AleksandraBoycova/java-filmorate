package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class GenreService extends AbstractService <Genre> {
    private GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage storage) {
        super(storage);
    }

    @Override
    protected void validate(Genre model) throws ValidationException {

    }

    public Genre create(Genre genre) throws ValidationException {
        return storage.create(genre);
    }

    public Genre update(Genre genre) throws Exception {
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
