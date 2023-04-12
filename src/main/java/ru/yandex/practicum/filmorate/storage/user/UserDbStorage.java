package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    public UserDbStorage(JdbcTemplate jdbcTemplate, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public User addUser(User user) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id_user");

        int userId = simpleJdbcInsert.executeAndReturnKey(user.toMap()).intValue();
        user.setId(userId);
        return user;
    }

    @Override
    public User modificationUser(User user) {
        String sql = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id_user = ?";

        int update = jdbcTemplate.update(sql,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        if (update > 0) {
            return user;
        } else {
            return null;
        }
    }

    @Override
    public Optional<User> getUserById(int userId) {
        String sql = "SELECT u.id_user, u.email, u.login, u.name, u.birthday FROM users u WHERE id_user = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userMapper, userId));
    }

    @Override
    public List<User> getFriends(int userId) {
        String sql = "SELECT u.id_user, u.email, u.login, u.name, u.birthday FROM users u " +
                "WHERE id_user IN (SELECT f.user2_id FROM friend f JOIN users u ON u.id_user = f.user1_id WHERE u.id_user = ?)";

        return jdbcTemplate.query(sql, userMapper, userId);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        final String sql = "INSERT INTO friend (user1_id, user2_id) VALUES (?, ?)";

        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void removeFromFriends(int userId, int friendId) {
        final String sql = "DELETE FROM friend WHERE user1_id = ? AND user2_id = ?";

        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public List<User> getUsers() {
        String sql = "SELECT u.id_user, u.email, u.login, u.name, u.birthday FROM users u";

        return jdbcTemplate.query(sql, userMapper);
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        String sql = "SELECT u.id_user, u.email, u.login, u.name, u.birthday FROM users u WHERE id_user IN " +
                "(SELECT f.user2_id FROM friend f JOIN users u ON u.id_user = f.user1_id WHERE u.id_user = ?)" +
                " AND id_user IN (SELECT f.user2_id FROM friend f JOIN users u ON u.id_user = f.user1_id WHERE u.id_user = ?)";

        return jdbcTemplate.query(sql, userMapper, userId, otherId);
    }
}
