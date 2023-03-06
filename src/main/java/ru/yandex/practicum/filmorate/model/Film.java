package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Film {

    private   int id; // идентификатор
    private   String name; // Имя
    private   String description; // описание
    private   LocalDate releaseDate; // дата релиза
    private   Integer duration; // продолжительность фильма


    public Film( String name, String description, LocalDate releaseDate, Integer duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
