package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest

public class FilmControllerTest {
    FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
    private Film film;


    @BeforeEach
    public void init() {
        film = Film.builder()
                .id(1)
                .name("Назад в будущее")
                .description("доктор который создал машину времени")
                .releaseDate(LocalDate.of(1985, 01, 01))
                .duration(145)
                .build();

    }

    @Test
    void validationFilm() throws ValidationException {
        Film newFilm = filmController.createFilm(film);
        List<Film> films = filmController.getFilms();

        assertNotNull(films, "Список фильмов пустой.");
        assertEquals(1, films.size(), "Количество фильмов не соответствует");
    }


    @Test
    void validationExceptionNullNameFilm() throws ValidationException {
        film.setName(null);
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertEquals("Название фильма не может быть пустым.", ex.getMessage());
    }

    @Test
    void validationExceptionNameFilm() throws ValidationException {
        film.setName("");
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertEquals("Название фильма не может быть пустым.", ex.getMessage());
    }
    @Test
    void validationExceptionTrimNameFilm() throws ValidationException {
        film.setName("      ");
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertEquals("Название фильма не может быть пустым.", ex.getMessage());
    }

    @Test
    void validationExceptionDescriptionFilm() {
        film.setDescription("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc ullamcorper rhoncus " +
                "sem nec commodo. Nam eu sollicitudin eros. Cras nec convallis erat. Nam at venenatis sem." +
                "                Nullam augue ante efficitur");
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertEquals("В фильме " +
                film.getName() + " описание больше 200 символов.", ex.getMessage());
    }


    @Test
    void validationExceptionReleaseDateFilm() {
        film.setReleaseDate(LocalDate.of(1895, 11, 28));

        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertEquals("В фильме " +
                film.getName() + " дата релиза — раньше 28 декабря 1895 года", ex.getMessage());
    }

    @Test
    void validationExceptionDurationFilm() {
        film.setDuration(-100);
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> filmController.createFilm(film)
        );
        assertEquals("В фильме " +
                film.getName() + " продолжительность должна быть положительной", ex.getMessage());
    }
}
