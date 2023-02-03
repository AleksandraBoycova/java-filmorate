package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;

public interface Storage <T>{
    T create(T t);
    T update(T t) throws Exception;
    T delete(Long id) throws Exception;
    Collection<T> getAll ();
    Optional<T> getById(Long id) throws Exception;
    boolean isExists(Long id);
}
