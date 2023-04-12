package ru.yandex.practicum.filmorate.storage.mpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Component
public class MpaDbStorage implements MpaStorage {

    private final Logger log = LoggerFactory.getLogger(MpaDbStorage.class);
    private final JdbcTemplate jdbcTemplate;
    private final MpaMapper mpaMapper;

    public MpaDbStorage(JdbcTemplate jdbcTemplate, MpaMapper mpaMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaMapper = mpaMapper;
    }


    @Override
    public List<Mpa> getMpas() {
        String sql = "SELECT m.id_mpa, m.name FROM mpa m ORDER BY id_mpa";

        return jdbcTemplate.query(sql, mpaMapper);
    }

    @Override
    public Optional<Mpa> getMpaById(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT m.id_mpa, m.name FROM mpa m WHERE id_mpa = ?", id);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getInt("id_mpa"),
                    mpaRows.getString("name")
            );
            log.info("MPA найден: {} {}", mpa.getId(), mpa.getName());
            return Optional.of(mpa);
        } else {
            log.info("MPA с id: {} не найден.", id);
            return Optional.empty();
        }
    }
}