package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractModel;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;

public abstract class AbstractService {

    protected Storage storage;

    @Autowired
    public AbstractService(Storage storage) {
        this.storage = storage;
    }

    public AbstractModel create(AbstractModel film) throws ValidationException {
        validate(film);
        return storage.create(film);
    }

    public AbstractModel update(AbstractModel film) throws Exception {
        validate(film);
        return storage.update(film);
    }

    public AbstractModel delete(Long id) throws Exception {
        return storage.delete(id);
    }

    public Collection<AbstractModel> getAll() {
        return storage.getAll();
    }

    public AbstractModel getById(Long id) throws Exception {
        return storage.getById(id);
    }

    protected abstract void validate(AbstractModel model) throws ValidationException;
}
