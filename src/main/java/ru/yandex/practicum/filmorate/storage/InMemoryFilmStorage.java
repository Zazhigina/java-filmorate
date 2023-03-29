package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private int idGenerator = 0;

    private int generateId() {
        return ++idGenerator;
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм {}", film);
        return film;
    }

    @Override
    public Film modificationFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм с id {} обновлен. Фильм: {}", film.getId(), film);
            return film;
        } else {
            log.debug("Ошибка обновления фильма: попытка обновления фильма с id {}.", film.getId());
            throw new FilmNotFoundException(String.format("Фильм с id: %s не найден!", film.getId()));
        }
    }

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Optional<Film> getFilmById(int filmId) {
        if (films.containsKey(filmId)) {
            return Optional.ofNullable(films.get(filmId));
        } else {
            log.debug("Ошибка при получении фильма по id {} его не существует.", filmId);
            throw new FilmNotFoundException(String.format("Фильм с id: %s не найден!", filmId));
        }
    }

    @Override
    public void addLike(int filmId, int userId) {
        for (Film film : getFilms()) {
            if (film.getId() == filmId) {
                film.getLikes().add(userId);
            }

        }

    }

    @Override
    public void removeLike(int filmId, int userId) {
        for (Film film : getFilms()) {
            if (film.getId() == filmId) {
                film.getLikes().remove(userId);
            }
        }
    }
}

