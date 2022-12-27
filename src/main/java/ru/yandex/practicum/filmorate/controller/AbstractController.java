package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.AbstractModel;
import ru.yandex.practicum.filmorate.service.AbstractService;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
public abstract class AbstractController {
    private AbstractService service;

    @Autowired
    public AbstractController(AbstractService service) {
        this.service = service;
    }

    public AbstractModel create(@Valid @RequestBody AbstractModel model)
            throws ValidationException {
        log.info("Создаем новый объект {}", model.getClass().getSimpleName());
        AbstractModel createdFilm = service.create(model);
        log.info("Создан {} {}", model.getClass().getSimpleName(), model.getId());
        return createdFilm;
    }

    public AbstractModel update(@Valid @RequestBody AbstractModel model)
            throws Exception {
        log.info("Обновляем объект {} {}", model.getClass().getSimpleName(), model.getId());
        AbstractModel updatedFilm = service.update(model);
        log.info("{} {} отредактирован", model.getClass().getSimpleName(), model.getId());
        return updatedFilm;
    }

    @DeleteMapping("{id}")
    public AbstractModel delete(@PathVariable Long id) throws Exception {
        log.info("Удаляем объект {}", id);
        AbstractModel deletedModel = service.delete(id);
        log.info("{} {} удален", deletedModel.getClass().getSimpleName(), deletedModel.getId());
        return deletedModel;
    }

    @GetMapping("{id}")
    public AbstractModel getById(@PathVariable Long id) throws Exception {
        AbstractModel model = service.getById(id);
        log.info("{} {} ", model.getClass().getSimpleName(), model.getId());
        return model;
    }

    @GetMapping
    public Collection<AbstractModel> getAll() throws Exception {
        return service.getAll();
    }
}
