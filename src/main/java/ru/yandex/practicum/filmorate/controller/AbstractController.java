package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractController <T>{
    protected final Map<Long, T> storage = new HashMap<>();


    public abstract void validate (T entity) throws ValidationException;


}
