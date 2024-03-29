package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    List<Film> getFilms();

    Film addFilm(Film film);

    Film modificationFilm(Film film);

    Optional<Film> getFilmById(int filmId);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);
}
