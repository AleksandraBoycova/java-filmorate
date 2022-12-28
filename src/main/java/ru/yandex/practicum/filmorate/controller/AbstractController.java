package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.service.AbstractService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
public abstract class AbstractController <T>{
    private AbstractService<T> service;

    public AbstractController(AbstractService<T> service) {
        this.service = service;
    }

    public T create(@Valid @RequestBody T model)
            throws ValidationException {
        log.info("Создаем новый объект {}", model.getClass().getSimpleName());
        T createdFilm = service.create(model);
        log.info("Создан {}", model.getClass().getSimpleName());
        return createdFilm;
    }

    public T update(@Valid @RequestBody T model)
            throws Exception {
        log.info("Обновляем объект {}", model.getClass().getSimpleName());
        T updatedFilm = service.update(model);
        log.info("{} отредактирован", model.getClass().getSimpleName());
        return updatedFilm;
    }

    @DeleteMapping("{id}")
    public T delete(@PathVariable Long id) throws Exception {
        log.info("Удаляем объект {}", id);
        T deletedModel = service.delete(id);
        log.info("{} удален", deletedModel.getClass().getSimpleName());
        return deletedModel;
    }

    @GetMapping("{id}")
    public T getById(@PathVariable Long id) throws Exception {
        T model = service.getById(id);
        log.info("{} найден по id", model.getClass().getSimpleName());
        return model;
    }

    @GetMapping
    public Collection<T> getAll() throws Exception {
        return service.getAll();
    }
}
