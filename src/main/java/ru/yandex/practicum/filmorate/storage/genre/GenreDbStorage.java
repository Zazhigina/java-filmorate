package ru.yandex.practicum.filmorate.storage.genre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;
import java.util.Optional;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final Logger log = LoggerFactory.getLogger(MpaDbStorage.class);
    private final GenreMapper genreMapper;

    public GenreDbStorage(JdbcTemplate jdbcTemplate, GenreMapper genreMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreMapper = genreMapper;
    }

    @Override
    public List<Genre> getGenres() {

        String sql = "SELECT g.id_genre, g.name FROM genre g ORDER BY id_genre";

        return jdbcTemplate.query(sql, genreMapper);
    }

    @Override
    public Optional<Genre> getGenreById(int id) {

        SqlRowSet genreRows = jdbcTemplate.queryForRowSet("SELECT g.id_genre, g.name FROM genre g WHERE id_genre = ?", id);
        if (genreRows.next()) {
            Genre genre = new Genre(
                    genreRows.getInt("id_genre"),
                    genreRows.getString("name")
            );
            log.info("Жанр найден: {} {}", genre.getId(), genre.getName());
            return Optional.of(genre);
        } else {
            log.info("Жанр с id: {} не найден.", id);
            return Optional.empty();
        }
    }
}