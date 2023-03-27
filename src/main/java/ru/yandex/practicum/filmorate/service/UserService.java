package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addToFriends(int userId, int friendId) throws ValidationException {
        if (userNotExists(userId)) {
            log.error("Ошибка в user service при добавлении в список друзей:" + " пользователь с id {} не найден.", userId);
            throw new UserNotFoundException(String.format("Пользователь с id: %s не найден!", userId));
        }

        if (userNotExists(friendId)) {
            log.error("Ошибка в user service при добавлении в список друзей:" + " пользователь с id {} не найден.", friendId);
            throw new UserNotFoundException(String.format("Пользователь id: %s не найден!", friendId));
        }

        if (userId == friendId) {
            log.debug("пытается добавить пользователей с таким же id в список друзей: id {}", userId);
            throw new ValidationException(String.format("Пользователи не могут быть друзьями сами себе, id: %s", userId));
        }
        for (User friend : userStorage.getFriends(userId)) {
            if (friend.getId() == friendId) {
                log.debug("пользователь с id: {} уже имеет пользователя с id: {} в списке друзей", userId, friendId);
                throw new ValidationException(String.format("Пользователь с id: %s  уже имеет пользователя с: %s " + "в списке друзей", userId, friendId));
            }
        }
        userStorage.addFriend(userId, friendId);
    }

    public void removeFromFriends(int userId, int friendId) {
        if (userNotExists(userId)) {
            log.error("Ошибка в user service при удалении из списка друзей: пользователь с id {} не найден.", userId);
            throw new UserNotFoundException(String.format("Пользователь с id: %s не найден!", userId));
        }

        if (userNotExists(friendId)) {
            log.error("Ошибка в user service при удалении из списка друзей: пользователь с id {} не найден.", friendId);
            throw new UserNotFoundException(String.format("Пользователь с id: %s не найден!", friendId));
        }

        if (userId == friendId) {
            log.debug("пытается удалить пользователей с таким же id в список друзей: id {}", userId);
            throw new UserNotFoundException(String.format("Пользователь не может сам себя удалить из друзей," + " id: %s", userId));
        }
        userStorage.removeFromFriends(userId, friendId);
    }

    public List<User> getFriends(int userId) {

        if (userNotExists(userId)) {
            log.error("user service получает пользователя по ошибке: user с id {} не найден.", userId);
            throw new UserNotFoundException(String.format("Пользователь с id: %s не найден!", userId));
        }

        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(int userId, int otherId) throws ValidationException {
        if (userNotExists(userId)) {
            log.error("user service получает пользователя по ошибке: user с id {} не найден.", userId);
            throw new UserNotFoundException(String.format("Пользователь с id: %s не найден!", userId));
        }

        if (userNotExists(otherId)) {
            log.error("user service получает пользователя по ошибке: user с id {} не найден.", otherId);
            throw new UserNotFoundException(String.format("Пользователь с id: %s не найден!", otherId));
        }

        if (userId == otherId) {
            log.debug("пытается найти общих друзей для пользователей с таким же id:: id {}", userId);
            throw new ValidationException(String.format("Пользователи с одинаковым идентификатором не могут быть друзьями, id: %s", userId));
        }
        return userStorage.getCommonFriends(userId, otherId);
    }

    public User addUser(User user) throws ValidationException {
        validation(user);
        return userStorage.addUser(user);
    }


    public void modificationUser(User user) throws ValidationException {
        validation(user);
        if (userNotExists(user.getId())) {
            log.error("user service получает пользователя по ошибке: user с id {} не найден.", user.getId());
            throw new UserNotFoundException(String.format("Пользователь с id: %s не найден!", user.getId()));
        }
        userStorage.modificationUser(user);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public Optional<User> getUserById(int userId) {
        if (userNotExists(userId)) {
            log.error("user service получает пользователя по ошибке: user с id {} не найден.", userId);
            throw new UserNotFoundException(String.format("Пользователь с id: %s не найден!", userId));
        }

        return userStorage.getUserById(userId);
    }

    public boolean userNotExists(int userId) {
        for (User user : userStorage.getUsers()) {
            if (userId == user.getId()) {
                return false;
            }
        }
        return true;
    }

    void validation(User user) throws ValidationException {
        if (!StringUtils.hasText(user.getEmail())) {
            log.warn("Неправильно ввели почту");
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        for (User user1 : getUsers()) {
            if (user1.getEmail().equals(user.getEmail())) {
                log.warn("Неправильно ввели почту");
                throw new ValidationException("Пользователь с электронной почтой " + user.getEmail() + " уже зарегистрирован.");
            }

        }
        if (!user.getEmail().contains("@")) {
            log.warn("Неправильно ввели почту");
            throw new ValidationException("Адрес электронной почты не содержит @.");
        }
        if (!StringUtils.hasText(user.getLogin())) {
            log.warn("Логин пустой");
            throw new ValidationException("Логин не может быть пустым.");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Неправильно ввели логин");
            throw new ValidationException("Логин не может содержать пробелы.");
        }
        if (!StringUtils.hasText(user.getName())) {
            log.warn("Не ввели имя,используется логин");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Не корректно ввели дату рождения");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
    }
}
