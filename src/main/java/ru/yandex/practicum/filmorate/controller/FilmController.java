package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final HashSet<Film> films = new HashSet<>();
    private int idGenerator = 1;

    @GetMapping()
    public HashSet<Film> findAllFilms() {
        return films;
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Название фильма id: {} отсутствует ", film.getId());
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() >= 200) {
            log.warn("Описание фильма {}: неподходящая длинна описания {} ", film.getName(), film.getDescription().length());
            throw new ValidationException("В фильме " +
                    film.getName() + " описание больше 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Фильм {}: неподходящая дата релиза {} ", film.getName(), film.getReleaseDate());
            throw new ValidationException("В фильме " +
                    film.getName() + " дата релиза — раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.warn("Фильм {} длительность отрицательная {} ", film.getName(), film.getDuration());
            throw new ValidationException("В фильме " +
                    film.getName() + " продолжительность должна быть положительной");
        }
        film.setId(idGenerator++);
        films.add(film);
        log.info("Добавили новый фильм {}", film.toString());
        return film;
    }

    @PutMapping
    public Film put(@RequestBody Film film) throws ValidationException {
        for (Film film1 : films) {
            if (film1.getId() == film.getId()) {
                films.remove(film1);
                films.add(film);
                log.info("Обновление фильма {}", film.toString());
                return film;
            } else {
                log.warn("Обновление не произошло");
                throw new ValidationException("Фильма :" + film.getName() + "не существует");
            }
        }
        return film;
    }
}
