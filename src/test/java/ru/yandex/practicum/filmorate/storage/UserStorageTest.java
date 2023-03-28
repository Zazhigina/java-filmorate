package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserStorageTest {


    private final UserStorage userStorage = new InMemoryUserStorage();

    private User user;
    private User friend;
    private User commonFriend;

    @BeforeEach
    public void init() {
        user = User.builder()
                .id(1)
                .name("user name")
                .login("user login")
                .email("user@mail.com")
                .birthday(LocalDate.of(2010, 7, 19))
                .build();

        friend = User.builder()
                .id(2)
                .name("friend name")
                .login("friend login")
                .email("friend@mail.com")
                .birthday(LocalDate.of(2005, 3, 23))
                .build();

        commonFriend = User.builder()
                .id(3)
                .name("common friend name")
                .login("common friend login")
                .email("commonfriend@mail.com")
                .birthday(LocalDate.of(2000, 1, 11))
                .build();
    }

    @Test
    public void testGetUsers() {
        userStorage.addUser(user);
        List<User> users = userStorage.getUsers();

        assertEquals(users.size(), 1);
    }

    @Test
    public void create() {
        User addedUser = userStorage.addUser(user);

        assertEquals(addedUser.getId(), 1);
        assertEquals(addedUser.getName(), "user name");
        assertEquals(addedUser.getLogin(), "user login");
        assertEquals(addedUser.getEmail(), "user@mail.com");
        assertEquals(addedUser.getBirthday(), LocalDate.of(2010, 7, 19));
    }

    @Test
    public void testUserUpdate() {
        User addedUser = userStorage.addUser(user);
        user.setName("updated name");

        userStorage.modificationUser(user);
        Optional<User> updatedUser = userStorage.getUserById(addedUser.getId());

        assertThat(updatedUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "updated name")
                );
    }

    @Test
    public void testGetUserById() {
        User addedUser = userStorage.addUser(user);

        Optional<User> userFormDb = userStorage.getUserById(addedUser.getId());

        assertThat(userFormDb)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", addedUser.getId())
                );
    }

    @Test
    public void testGetUserFriends() {
        User addedUser = userStorage.addUser(user);
        User addedFriend = userStorage.addUser(friend);

        List<User> noFriends = userStorage.getFriends(addedFriend.getId());

        assertEquals(noFriends.size(), 0);

        userStorage.addFriend(addedUser.getId(), addedFriend.getId());
        List<User> friends = userStorage.getFriends(addedUser.getId());

        assertEquals(friends.size(), 1);
    }

    @Test
    public void testGetCommonFriends() {
        User addedUser = userStorage.addUser(user);
        User addedFriend = userStorage.addUser(friend);
        User addedCommonFriend = userStorage.addUser(commonFriend);

        userStorage.addFriend(addedUser.getId(), addedCommonFriend.getId());
        userStorage.addFriend(addedFriend.getId(), addedCommonFriend.getId());

        List<User> commonFriends = userStorage.getCommonFriends(addedUser.getId(), addedFriend.getId());

        assertEquals(commonFriends.size(), 1);
    }

    @Test
    public void testAddFriend() {
        User addedUser = userStorage.addUser(user);
        User addedFriend = userStorage.addUser(friend);

        userStorage.addFriend(addedUser.getId(), addedFriend.getId());

        List<User> friends = userStorage.getFriends(addedUser.getId());

        assertEquals(friends.size(), 1);
    }

    @Test
    public void removeFromFriends() {
        User addedUser = userStorage.addUser(user);
        User addedFriend = userStorage.addUser(friend);

        userStorage.addFriend(addedUser.getId(), addedFriend.getId());
        List<User> friends = userStorage.getFriends(addedUser.getId());

        assertEquals(friends.size(), 1);

        userStorage.removeFromFriends(addedUser.getId(), addedFriend.getId());
        List<User> noFriends = userStorage.getFriends(addedFriend.getId());

        assertEquals(noFriends.size(), 0);
    }

}
