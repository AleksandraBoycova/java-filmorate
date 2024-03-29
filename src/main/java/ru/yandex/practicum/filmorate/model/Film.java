package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull
    private LocalDate releaseDate;
    private int duration;
    private Set <Long> likes = new HashSet<>();
}
