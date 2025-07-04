package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class UserDbStorage extends BaseStorage<User> implements UserStorage {
    private static final String GET_USERS = "SELECT * FROM users";
    private static final String GET_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?";
    private static final String ADD_USER = "INSERT INTO users(email, login, name, birthday)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? " +
            "WHERE user_id = ?";
    private static final String DELETE_FRIEND = "DELETE FROM users_friends WHERE user_id = ? AND friend_id = ?";
    private static final String ADD_FRIEND = "INSERT INTO users_friends (user_id, friend_id) VALUES (?, ?)";
    private static final String GET_FRIENDS = "SELECT u.user_id, u.email, u.login, u.name, u.birthday " +
            "FROM users_friends uf " +
            "JOIN users u ON uf.friend_id = u.user_id " +
            "WHERE uf.user_id = ?";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> getUsers() {
        return findMany(GET_USERS);
    }

    public User getUserById(long userId) {
        return findOne(GET_USER_BY_ID, userId);
    }

    public User addUser(User user) {
        long id = insert(
                ADD_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setFriends(getFriends(user.getId()).stream().map(User::getId).collect(Collectors.toSet()));
        user.setId(id);
        return user;
    }

    public User updateUser(User user) {
        update(
                UPDATE_USER,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    public List<User> getFriends(Long userId) {
        return findMany(GET_FRIENDS, userId);
    }

    public void addFriend(long userId, long friendId) {
        insert(ADD_FRIEND, userId, friendId);
    }

    public void deleteFriend(long userId, long friendId) {
        update(DELETE_FRIEND, userId, friendId);
    }
}
