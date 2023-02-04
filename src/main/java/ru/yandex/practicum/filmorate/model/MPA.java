package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class MPA {
    private Long id;
    private String name;
    private String description;

    public MPA(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public MPA() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
