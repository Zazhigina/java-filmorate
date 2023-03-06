package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")

public class UserController {

    private final HashSet<User> users = new HashSet<>();
    private int idGenerator = 1;

    @GetMapping()
    public HashSet<User> findAllUsers() {
        return users;
    }

    @PostMapping
    public User create(@RequestBody User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            log.warn("Неправильно ввели почту");
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        for (User user1 : users) {
            if (user1.getEmail().equals(user.getEmail())) {
                log.warn("Неправильно ввели почту");
                throw new ValidationException("Пользователь с электронной почтой " +
                        user.getEmail() + " уже зарегистрирован.");
            }

        }
        if (!user.getEmail().contains("@")) {
            log.warn("Неправильно ввели почту");
            throw new ValidationException("Адрес электронной почты не содержит @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            log.warn("Логин пустой");
            throw new ValidationException("Логин не может быть пустым.");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Неправильно ввели логин");
            throw new ValidationException("Логин не может содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            log.warn("Не ввели имя,используется логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Не корректно ввели дату рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        user.setId(idGenerator++);
        users.add(user);
        log.info(user.toString());
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) throws ValidationException {
        for (User user1 : users) {
            if (user1.getId() == user.getId()) {
                users.remove(user1);
                users.add(user);
                log.info(user.toString());
                return user;
            }else {
                log.warn("Обновление не произошло");
                throw new ValidationException("Пользователя с id:"+ user.getId()+"не существует");
            }
        }
        return user;
    }
}
