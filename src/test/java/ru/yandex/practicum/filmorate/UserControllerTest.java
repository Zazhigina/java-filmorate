package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest

public class UserControllerTest {

    UserController userController = new UserController();
    User user;
    User user1;
    User user2;
    User user3;
    User user4;
    User user5;
    User user6;
    User user7;
    User user8;


    @BeforeEach
    public void beforeEach() {

        user = new User("katty.1996@mail.ru", "Katty", LocalDate.of(1996, 1, 3));
        user1 = new User("katty.1996 mail.ru", "K atty", LocalDate.of(1996, 1, 3));
        user2 = new User("katty.1996@mail.ru", "Katty", LocalDate.of(1996, 1, 3));
        user3 = new User(null, "Katty", LocalDate.of(1996, 1, 3));
        user4 = new User("katty.1996@mail.ru", "Katty", LocalDate.of(1996, 1, 3));
        user5 = new User("katty.1996@mail.ru", null, LocalDate.of(1996, 1, 3));
        user6 = new User("katty.1996@mail.ru", "Katty", LocalDate.of(1996, 1, 3));
        user7 = new User("katty.1996@mail.ru", "Katty", LocalDate.of(2023, 5, 3));
        user8 = new User("katty.1996@mail.ru", "K atty", LocalDate.of(1996, 1, 3));

    }

    @Test
    void validationUser() throws ValidationException {
        User newUser = userController.create(user);
        HashSet<User> users = userController.findAllUsers();

        assertNotNull(users, "Список пользователей пустой.");
        assertEquals(1, users.size(), "Количество пользователей не соответствует");
    }


    @Test
    void validationExceptionEmailUser() {
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.create(user3)
        );
        assertEquals("Адрес электронной почты не может быть пустым.", ex.getMessage());

    }

    @Test
    void validationExceptionEmailContainsUser() throws ValidationException {
        userController.create(user);
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.create(user4)
        );
        assertEquals("Пользователь с электронной почтой katty.1996@mail.ru уже зарегистрирован.", ex.getMessage());
    }

    @Test
    void validationExceptionEmail() {
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.create(user1)
        );
        assertEquals("Адрес электронной почты не содержит @.", ex.getMessage());

    }

    @Test
    void validationExceptionNullLoginUser() {
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.create(user5)
        );
        assertEquals("Логин не может быть пустым.", ex.getMessage());
    }

    @Test
    void validationExceptionTrimLoginUser() {
        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.create(user8)
        );
        assertEquals("Логин не может содержать пробелы.", ex.getMessage());
    }

    @Test
    void validationExceptionNameUser() throws ValidationException {
        User newUser = userController.create(user6);
        assertEquals("Katty", user6.getName());
    }

    @Test
    void validationExceptionBirthdayUser() {

        ValidationException ex = assertThrows(

                ValidationException.class,
                () -> userController.create(user7)
        );
        assertEquals("Дата рождения не может быть в будущем", ex.getMessage());

    }
}
