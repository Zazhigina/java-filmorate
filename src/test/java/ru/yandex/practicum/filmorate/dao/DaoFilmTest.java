package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DaoFilmTest {

    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private Film film;
    private User user;
    @BeforeEach
    public void init() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1, null));
        genres.add(new Genre(2, null));
        genres.add(new Genre(3, null));

        film = Film.builder()
                .id(1)
                .name("film name")
                .description("film description")
                .releaseDate(LocalDate.of(2000, 1, 1))
                .duration(100)
                .mpa(new Mpa(4, null))
                .genres(genres)
                .build();

        user = User.builder()
                .id(1)
                .name("user name")
                .login("user login")
                .email("user@mail.com")
                .birthday(LocalDate.of(2010, 7, 19))
                .build();
    }


    @Test
    public void testGetFilms() {
        filmStorage.addFilm(film);
        List<Film> films = filmStorage.getFilms();

        assertEquals(films.size(), 1);
    }

    @Test
    public void testCreateFilm() {
        Film addedFilm = filmStorage.addFilm(film);

        assertEquals(addedFilm.getId(), 4);
        assertEquals(addedFilm.getName(), "film name");
        assertEquals(addedFilm.getDescription(), "film description");
        assertEquals(addedFilm.getReleaseDate(), LocalDate.of(2000, 1, 1));
        assertEquals(addedFilm.getDuration(), 100);
    }

    @Test
    public void testFilmUpdate() {
        Film addedFilm = filmStorage.addFilm(film);
        film.setName("updated name");

        filmStorage.modificationFilm(film);
        Optional<Film> updatedFilm = filmStorage.getFilmById(addedFilm.getId());

        assertThat(updatedFilm)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "updated name")
                );
    }

    @Test
    public void testGetFilmById() {
        Film addedFilm = filmStorage.addFilm(film);

        Optional<Film> filmFromDb = filmStorage.getFilmById(addedFilm.getId());

        assertThat(filmFromDb)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", addedFilm.getId())
                );
    }

    @Test
    public void testAddLikeToFilm() {
        User addedUser = userStorage.addUser(user);

        Film addedFilm = filmStorage.addFilm(film);

        filmStorage.addLike(addedFilm.getId(), addedUser.getId());
        Optional<Film> optFilm = filmStorage.getFilmById(addedFilm.getId());
        Film filmFromDb;

        if (optFilm.isPresent()) {
            filmFromDb = optFilm.get();

            assertEquals(filmFromDb.getLikes().size(), 1);
        } else {
            fail();
        }
    }

    @Test
    public void testRemoveLikeFromFilm() {
        User addedUser = userStorage.addUser(user);

        Film addedFilm = filmStorage.addFilm(film);

        filmStorage.addLike(addedFilm.getId(), addedUser.getId());
        filmStorage.removeLike(addedFilm.getId(), user.getId());

        Optional<Film> optFilm = filmStorage.getFilmById(addedFilm.getId());
        Film filmFromDb;

        if (optFilm.isPresent()) {
            filmFromDb = optFilm.get();

            assertEquals(filmFromDb.getLikes().size(), 0);
        } else {
            fail();
        }
    }
}
