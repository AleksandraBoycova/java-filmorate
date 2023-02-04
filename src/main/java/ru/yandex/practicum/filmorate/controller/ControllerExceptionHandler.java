package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exception.ApplicationError;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.sql.SQLException;
import java.util.List;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ApplicationError> handleValidationException(ValidationException e) {
        ApplicationError applicationError = new ApplicationError(HttpStatus.BAD_REQUEST, e);
       log.error("Validation Exception Thrown");
        return new ResponseEntity<>(applicationError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler( NotFoundException.class)
    public ResponseEntity<ApplicationError> handleNotFoundException(NotFoundException e) {
        ApplicationError applicationError = new ApplicationError(HttpStatus.NOT_FOUND, "Объект не найден", e);
        log.error("Not found exception thrown");
        return new ResponseEntity<>(applicationError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler()
    public ResponseEntity<ApplicationError> handleException(Exception e) {
        ApplicationError applicationError = new ApplicationError(HttpStatus.INTERNAL_SERVER_ERROR, "Произошла ошибка", e);
        log.error("Unexpected exception thrown");
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
        log.error("Method Argument Not Valid Exception");
        return new ResponseEntity<>(applicationError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler()
    public ResponseEntity<ApplicationError> handleBadSqlGrammarException(BadSqlGrammarException e) {
        StringBuilder    errorMessage = new StringBuilder();
        SQLException sqlException = e.getSQLException();
        ApplicationError applicationError = new ApplicationError(HttpStatus.BAD_REQUEST, "Ошибка базы данных.", sqlException);
        log.error("Bad Sql Grammar Exception Exception");
        return new ResponseEntity<>(applicationError, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
