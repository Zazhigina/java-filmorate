package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<Mpa> getMpas() {
        return mpaStorage.getMpas();
    }

    public Mpa getMpaById(int mpaId) {
        Optional<Mpa> mpaById = mpaStorage.getMpaById(mpaId);
        if (mpaById.isEmpty()) {
            log.info("mpa service получает не верные данные. Ошибка в id {} такого рейтинга не существует!", mpaId);
            throw new MpaNotFoundException(String.format("Рейтинг с id: %s  не найден", mpaId));
        }
        return mpaById.get();
    }
}
