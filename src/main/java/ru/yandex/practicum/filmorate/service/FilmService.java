package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film createFilm(Film film) throws ValidationException {
        validation(film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) throws ValidationException {
        validation(film);
        if (filmNotExists(film.getId())) {
            log.error("user service получает фильм по ошибке: film с id {} не найден.", film.getId());
            throw new NotFoundException(String.format("Фильм с id: %s не найден!", film.getId()));
        }
        return filmStorage.modificationFilm(film);
    }

    public Optional<Film> getFilmById(int filmId) {
        if (filmNotExists(filmId)) {
            log.error("film service получает фильм по ошибке: film с id {} не найден!", filmId);
            throw new NotFoundException(String.format("Фильм с id: %s не найден!", filmId));
        }
        return filmStorage.getFilmById(filmId);
    }

    public void addLike(int filmId, int userId) throws ValidationException {
        validateFilmAndUser(filmId, userId);
        Optional<Film> filmById = filmStorage.getFilmById(filmId);
        if (filmById.isPresent()) {
            if (filmById.get().getLikes().contains(userId)) {
                log.error("Ошибка в film service при добавлений лайка пользователем id {} фильму c id {} уже был добавлен", userId, filmId);
                throw new ValidationException(String.format("Пользователь с id %s уже был добавлен лайк " +
                        "фильму с id %s.", userId, filmId));
            }
        }
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) throws ValidationException {
        validateFilmAndUser(filmId, userId);
        Optional<Film> filmById = filmStorage.getFilmById(filmId);
        if (filmById.isPresent()) {
            if (!(filmById.get().getLikes().contains(userId))) {
                log.error("Ошибка в film service при удалении лайка пользователем id {} фильму c id {} лайк пользователя" +
                        " в этом фильме нет ", userId, filmId);
                throw new ValidationException(String.format("Пользователь с id %s не ставил лайк " +
                        "фильму с id %s.", userId, filmId));
            } else {
                filmStorage.removeLike(filmId, userId);
            }
        }
    }


    public List<Film> getPopularFilms(int count) throws ValidationException {
        if (count <= 0) {
            log.error("Ошибка в film service при получении популярного списка фильмов: " +
                    "количество не может быть отрицательным, количество: {}.", count);
            throw new ValidationException(String.format("количество фильмов не может быть отрицательным," +
                    " количество: %s.", count));
        }

        if (count > filmStorage.getFilms().size()) {
            count = filmStorage.getFilms().size();
        }
        return filmStorage.getFilms()
                .stream()
                .sorted((o1, o2) -> o2.getLikes().size() - o1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());

    }

    public boolean filmNotExists(int filmId) {
        for (Film film : filmStorage.getFilms()) {
            if (filmId == film.getId()) {
                return false;
            }
        }
        return true;
    }

    public boolean userExists(int userId) {
        for (User user : userStorage.getUsers()) {
            if (userId == user.getId()) {
                return true;
            }
        }
        return false;
    }

    private void validateFilmAndUser(int filmId, int userId) {
        if (filmNotExists(filmId)) {
            log.error("user service получает фильм по ошибке: film с id {} не найден.", filmId);
            throw new NotFoundException(String.format("Фильм с id: %s не найден!", filmId));
        }
        if (!userExists(userId)) {
            log.error("user service получает пользователя по ошибке: user с id {} не найден.", userId);
            throw new NotFoundException(String.format("Пользователь с id: %s не найден!", userId));
        }
    }


    void validation(Film film) throws ValidationException {
        if (!StringUtils.hasText(film.getName())) {
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
    }
}
