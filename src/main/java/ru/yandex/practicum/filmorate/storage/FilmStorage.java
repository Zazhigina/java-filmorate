package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getFilms();

    Film addFilm(Film film);

    Film modificationFilm(Film film) throws ValidationException;

    Optional<Film> getFilmById(int filmId);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);
}
