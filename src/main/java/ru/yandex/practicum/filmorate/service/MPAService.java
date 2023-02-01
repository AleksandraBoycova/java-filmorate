package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.Storage;

@Service
public class MPAService extends AbstractService <MPA>{
    @Autowired
    public MPAService(MpaStorage storage) {
        super(storage);
    }

    @Override
    protected void validate(MPA model) throws ValidationException {

    }
}
