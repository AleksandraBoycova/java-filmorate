package ru.yandex.practicum.filmorate.controller;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.ApplicationError;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.util.List;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApplicationError> handleValidationException(ValidationException e) {
        ApplicationError applicationError = new ApplicationError(HttpStatus.BAD_REQUEST, e);
        return new ResponseEntity<>(applicationError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({FilmNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ApplicationError> handleNotFoundException(Exception e) {
        ApplicationError applicationError = new ApplicationError(HttpStatus.NOT_FOUND, "Объект не найден", e);
        return new ResponseEntity<>(applicationError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler()
    public ResponseEntity<ApplicationError> handleException(Exception e) {
        ApplicationError applicationError = new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла ошибка", e);
        return new ResponseEntity<>(applicationError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationError> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder    errorMessage = new StringBuilder();
        List<FieldError> allErrors    = e.getBindingResult().getFieldErrors();
        for (FieldError error : allErrors) {
            errorMessage.append("Field: ").append(error.getField());
            errorMessage.append(", rejected value: ").append(error.getRejectedValue());
            errorMessage.append(" error: ").append(error.getDefaultMessage());
        }
        ApplicationError applicationError = new ApplicationError(HttpStatus.BAD_REQUEST, "Ошибка валидации. " + errorMessage, e);
        MethodParameter  parameter        = e.getParameter();
        parameter.getAnnotatedElement();
        return new ResponseEntity<>(applicationError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
