package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.Collection;
import java.util.Optional;

public abstract class AbstractService <T>{

    protected Storage <T> storage;

    @Autowired
    public AbstractService(Storage<T> storage) {
        this.storage = storage;
    }

    public T create(T t) throws ValidationException {
        validate(t);
        return storage.create(t);
    }

    public T update(T t) throws Exception {
        validate(t);
        return storage.update(t);
    }

    public T delete(Long id) throws Exception {
        return storage.delete(id);
    }

    public Collection<T> getAll() {
        return storage.getAll();
    }

    public T getById(Long id) throws Exception {
        Optional<T> optionalT = storage.getById(id);
        if (optionalT.isEmpty()) {
            throw new NotFoundException("Object not found");
        }
        return optionalT.get();
    }

    protected abstract void validate(T model) throws ValidationException;
}
