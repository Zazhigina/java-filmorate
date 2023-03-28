package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int idGenerator = 0;

    @Override
    public User addUser(User user) {
        user.setId(generateId());
        users.put(user.getId(), user);
        log.info("Новый пользователь добавлен {}", user);
        return user;

    }

    @Override
    public void modificationUser(User user) {
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Пользователь с id {} обновлен. Пользователь: {}", user.getId(), user);
        } else {
            log.debug("Ошибка обновления пользователя: попытка обновления пользователя с id {}.", user.getId());
            throw new UserNotFoundException(String.format("Пользователь с id: %s не найден!", user.getId()));
        }
    }

    @Override
    public Optional<User> getUserById(int userId) {
        if (users.containsKey(userId)) {
            return Optional.ofNullable(users.get(userId));
        } else {
            log.debug("Ошибка при получении пользователя по id {} его не существует.", userId);
            throw new UserNotFoundException(String.format("Пользователь с id: %s не найден!", userId));
        }
    }


    @Override
    public List<User> getFriends(int userId) {
        return getUsers().stream().filter(u -> u.getFriends().contains(userId)).collect(Collectors.toList());
    }

    @Override
    public void addFriend(int userId, int friendId) {
        for (User user : getUsers()) {
            if (userId == user.getId()) {
                user.getFriends().add(friendId);
            }
        }

        for (User user : getUsers()) {
            if (friendId == user.getId()) {
                user.getFriends().add(userId);
            }
        }
    }


    @Override
    public void removeFromFriends(int userId, int friendId) {
        for (User user : getUsers()) {
            if (userId == user.getId()) {
                user.getFriends().remove(friendId);
            }
        }

        for (User user : getUsers()) {
            if (friendId == user.getId()) {
                user.getFriends().remove(userId);
            }
        }
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        ArrayList<User> commonFriends = new ArrayList<>();
        Optional<User> user = getUserById(userId);
        Set<Integer> friendsOfUser = new HashSet<>();
        if (user.isPresent()) {
            friendsOfUser = user.get().getFriends();
        }
        Optional<User> otherUser = getUserById(otherId);
        Set<Integer> friendsOfOtherUser = new HashSet<>();
        if (otherUser.isPresent()) {
            friendsOfOtherUser = otherUser.get().getFriends();
        }
        if (!(Collections.disjoint(friendsOfUser, friendsOfOtherUser))) {

            for (Integer commonUser : friendsOfUser) {
                if (friendsOfOtherUser.contains(commonUser)) {
                    Optional<User> userById = getUserById(commonUser);
                    userById.ifPresent(commonFriends::add);
                }
            }
        }
        return commonFriends;
    }

    private int generateId() {
        return ++idGenerator;
    }

}
