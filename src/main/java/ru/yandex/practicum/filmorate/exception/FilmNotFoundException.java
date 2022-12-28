package ru.yandex.practicum.filmorate.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({"cause", "stackTrace", "suppressed", "localizedMessage"})
public class FilmNotFoundException extends Exception implements ApiSubError {
    public FilmNotFoundException(String message) {
        super(message);
    }
}
