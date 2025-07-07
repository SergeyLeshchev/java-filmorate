package ru.yandex.practicum.filmorate.dal.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User getUserById(long id);

    User addUser(User user);

    User updateUser(User user);

    List<User> getFriends(Long userId);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);
}
