package ru.yandex.practicum.filmorate.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties({"cause", "stackTrace", "suppressed", "localizedMessage"})
public class NotFoundException extends Exception implements ApiSubError{
    public NotFoundException(String message) {
        super(message);
    }
}
