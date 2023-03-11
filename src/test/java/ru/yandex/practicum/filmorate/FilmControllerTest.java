package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;


import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {
    FilmController filmController = new FilmController();
    Film film;
    Film film1;
    Film film2;
    Film film3;
    Film film4;

    @BeforeEach
    public void beforeEach() {

        film = new Film( "Назад в будущее", "доктор который создал машину времени", LocalDate.of(1985, 01, 01), 145);
        film1 = new Film( "", "доктор который создал машину времени", LocalDate.of(1985, 01, 01), 145);
        film2 = new Film( "Назад в будущее", "американский научно-фантастический фильм режиссёра Роберта Земекиса. Сценарий написали Земекис и Боб Гейл, исполнительные продюсеры — Стивен Спилберг, Фрэнк Маршалл и Кэтлин Кеннеди. Главные роли исполнили Майкл Джей Фокс, Кристофер Ллойд, Лиа Томпсон, Криспин Гловер и Томас Ф. Уилсон. По сюжету, подросток Марти Макфлай случайно попадает из 1985 года в 1955, где мешает знакомству своих юных родителей.", LocalDate.of(1985, 01, 01), 145);
        film3 = new Film( "Аватар", "американский эпический научно-фантастический фильм 2009 года сценариста и режиссёра Джеймса Кэмерона", LocalDate.of(1777, 07, 07), 242);
        film4 = new Film( "Аватар2", "Продолжение 1 части", LocalDate.of(2022, 12, 10), -10);
    }

    @Test
    void validationFilm() throws ValidationException {
        Film newFilm = filmController.create(film);
        HashSet< Film> films = filmController.findAllFilms();

        assertNotNull(films, "Список фильмов пустой.");
        assertEquals(1, films.size(), "Количество фильмов не соответствует");
    }


    @Test
    void validationExceptionNullNameFilm() throws ValidationException {
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> filmController.create(film1)
        );
        assertEquals("Название фильма не может быть пустым.", ex.getMessage());
    }

    @Test
    void validationExceptionDescriptionFilm() {
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> filmController.create(film2)
        );
        assertEquals("В фильме " +
                film.getName() + " описание больше 200 символов.", ex.getMessage());
    }


    @Test
    void validationExceptionReleaseDateFilm() {
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> filmController.create(film3)
        );
        assertEquals("В фильме " +
                film3.getName() + " дата релиза — раньше 28 декабря 1895 года", ex.getMessage());
    }

    @Test
    void validationExceptionDurationFilm() {
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> filmController.create(film4)
        );
        assertEquals("В фильме " +
                film4.getName() + " продолжительность должна быть положительной", ex.getMessage());
    }
}
