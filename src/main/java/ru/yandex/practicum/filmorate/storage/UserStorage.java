package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);

    void modificationUser(User user);

    Optional<User> getUserById(int userId);

    List<User> getFriends(int userId);

    void addFriend(int userId, int friendId);

    void removeFromFriends(int userId, int friendId);

    List<User> getUsers();

    List<User> getCommonFriends(int userId, int otherId);
}