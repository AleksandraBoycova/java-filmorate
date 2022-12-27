package ru.yandex.practicum.filmorate.exception;

import lombok.Data;

@Data
public class FilmNotFoundException extends Exception implements ApiSubError {
    public FilmNotFoundException(String message) {
        super(message);
    }
}
