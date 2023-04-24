package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest

public class UserControllerTest {

    UserController userController = new UserController(new UserService(new InMemoryUserStorage()));
    private User user;


    @BeforeEach
    public void init() {
        user = User.builder()
                .id(1)
                .name("John Smith")
                .login("login")
                .email("example@email.com")
                .birthday(LocalDate.of(1913, 5, 17))
                .build();
    }


    @Test
    void validationUser() throws ValidationException {
        User newUser = userController.createUser(user);
        List<User> users = userController.findAllUsers();

        assertNotNull(users, "Список пользователей пустой.");
        assertEquals(1, users.size(), "Количество пользователей не соответствует");
    }


    @Test
    void validationExceptionNullEmailUser() {
        user.setEmail(null);
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.createUser(user)
        );
        assertEquals("Адрес электронной почты не может быть пустым.", ex.getMessage());

    }

    @Test
    void validationExceptionEmailUser() {
        user.setEmail("");
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.createUser(user)
        );
        assertEquals("Адрес электронной почты не может быть пустым.", ex.getMessage());

    }

    @Test
    void validationExceptionTrimEmailUser() {
        user.setEmail("      ");
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.createUser(user)
        );
        assertEquals("Адрес электронной почты не может быть пустым.", ex.getMessage());

    }


    @Test
    void validationExceptionEmail() {
        user.setEmail("abcdefghijklmnop");
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.createUser(user)
        );
        assertEquals("Адрес электронной почты не содержит @.", ex.getMessage());

    }

    @Test
    void validationExceptionLoginUser() {
        user.setLogin("");
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.createUser(user)
        );
        assertEquals("Логин не может быть пустым.", ex.getMessage());
    }

    @Test
    void validationExceptionNullLoginUser() {
        user.setLogin(null);
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.createUser(user)
        );
        assertEquals("Логин не может быть пустым.", ex.getMessage());
    }

    @Test
    void validationExceptionTrimLoginUser() {
        user.setLogin("Katt y");
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.createUser(user)
        );
        assertEquals("Логин не может содержать пробелы.", ex.getMessage());
    }

    @Test
    void validationExceptionNameUser() throws ValidationException {
        user.setName("");
        userController.createUser(user);

        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void validationExceptionBirthdayUser() throws ValidationException {

        user.setBirthday(LocalDate.MAX);
        ValidationException ex = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Дата рождения не может быть в будущем", ex.getMessage());

        user.setBirthday(LocalDate.now());
        userController.createUser(user);

        assertEquals(userController.findAllUsers().size(), 1);

    }
}
