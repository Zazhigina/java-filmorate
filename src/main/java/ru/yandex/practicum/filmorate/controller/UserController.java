package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping()
    public List<User> findAllUsers() {
        return userService.getUsers();
    }


    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        userService.modificationUser(user);
        return user;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) throws ValidationException {
        return userService.addUser(user);
    }


    @GetMapping("/{id}")
    public Optional<User> getUser(@PathVariable("id") int userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@PathVariable("id") int userId,
                             @PathVariable("friendId") int friendId) throws ValidationException {
        userService.addToFriends(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFromFriends(@PathVariable("id") int userId,
                                  @PathVariable("friendId") int friendId) {
        userService.removeFromFriends(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") int userId) {
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") int userId,
                                       @PathVariable("otherId") int otherId) throws ValidationException {
        return userService.getCommonFriends(userId, otherId);
    }


}
