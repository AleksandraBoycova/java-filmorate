package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.Storage;

@Service
public class MPAService extends AbstractService <MPA>{
    public MPAService(Storage<MPA> storage) {
        super(storage);
    }

    @Override
    protected void validate(MPA model) throws ValidationException {

    }
}
