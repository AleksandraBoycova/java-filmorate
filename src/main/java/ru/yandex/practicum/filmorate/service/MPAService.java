package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;
import java.util.Optional;

@Service
public class MPAService extends AbstractService <MPA>{
    @Autowired
    public MPAService(MpaStorage storage) {
        super(storage);
    }

    @Override
    protected void validate(MPA model) throws ValidationException {

    }

    public MPA create(MPA mpa) throws ValidationException {
        return storage.create(mpa);
    }

    public MPA update(MPA mpa) throws Exception {
        return storage.update(mpa);
    }

    public MPA delete(Long id) throws Exception {
        return storage.delete(id);
    }

    public Collection<MPA> getAll() {
        return storage.getAll();
    }

    public MPA getById(Long id) throws Exception {
        Optional<MPA> optionalMPA = storage.getById(id);
        if (optionalMPA.isEmpty()) {
            throw new RuntimeException();
        }
        return optionalMPA.get();
    }
}
