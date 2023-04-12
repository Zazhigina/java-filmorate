package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;


    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }


    public Genre getGenreById(int genreId) {
        Optional<Genre> genreById = genreStorage.getGenreById(genreId);
        if (genreById.isEmpty()) {
            log.info("genre service получает не верные данные. Ошибка в id {} такого жанра не существует!", genreId);
            throw new GenreNotFoundException(String.format("Жанр с id: %s был не найден!", genreId));
        }
        return genreById.get();
    }


}
