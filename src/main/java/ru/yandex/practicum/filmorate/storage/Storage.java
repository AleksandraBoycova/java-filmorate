package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.AbstractModel;

import java.util.Collection;

public interface Storage {
    AbstractModel create(AbstractModel film);
    AbstractModel update(AbstractModel film) throws Exception;
    AbstractModel delete(Long id) throws Exception;
    Collection<AbstractModel> getAll ();
    AbstractModel getById(Long id) throws Exception;
}
