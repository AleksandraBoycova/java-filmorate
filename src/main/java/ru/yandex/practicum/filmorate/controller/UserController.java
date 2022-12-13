package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping ("users")
public class UserController {
    private final Map<Long, User> storage = new HashMap<>();
    private static long counter = 1L;

    @PostMapping
    public User createUser (@Valid @RequestBody User user)
            throws ValidationException {
        validate(user);
        user.setId(counter++);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        storage.put(user.getId(), user);
        log.info("Создан пользователь {}",user.getName());
        return user;
    }

    @PutMapping
    public User updateUser (@Valid @RequestBody User user)
            throws ValidationException {
        validate(user);
        Long id = user.getId();
        if (storage.containsKey(id)){
            storage.put(id, user);
            log.info("Пользователь {} изменен", user.getName());
        }
        else {
            throw new ValidationException ("Пользователь с id "+ id + " не найден");
        }
        return user;
    }
    @GetMapping
    public Collection<User> getAllUsers (){
        return storage.values();
    }

    private void validate (User user) throws ValidationException {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
           throw new  ValidationException ("Email введен не верно");
        }
        if(user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Введен не верный логин");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Не верная дата рождения");
        }

    }

}
