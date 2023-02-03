package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequestMapping("mpa")
public class MPAController extends AbstractController<MPA>{
    private MPAService mpaService;

    @Autowired
    public MPAController(MPAService mpaService) {
        super(mpaService);
        this.mpaService = mpaService;
    }

    @PostMapping
    public MPA createMPA(@Valid @RequestBody MPA mpa)
            throws ValidationException {
        return super.create(mpa);
    }

    @PutMapping
    public MPA updateMPA(@Valid @RequestBody MPA mpa)
            throws Exception {
        return super.update(mpa);
    }
}

