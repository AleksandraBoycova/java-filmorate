package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;

public interface Storage <T>{
    T create(T t);
    T update(T t) throws Exception;
    T delete(Long id) throws Exception;
    Collection<T> getAll ();
    T getById(Long id) throws Exception;
}
