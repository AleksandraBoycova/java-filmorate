package ru.yandex.practicum.filmorate.model;

public class Genre {
    private Long id;
    private String name;

    public Genre(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre() {
    }

    public void setId(long id) {
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
}

