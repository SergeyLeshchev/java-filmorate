package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public void addFriend(Long userId, Long friendId) {
        log.info("addFriend() - получен запрос от пользователя с id {} на добавление в друзья пользователя c id {}",
                userId, friendId);
        userStorage.getUser(userId).getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(userId);
        log.info("addFriend() - выполнен запрос от пользователя с id {} на добавление в друзья пользователя c id {}",
                userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        log.info("deleteFriend() - получен запрос от пользователя с id {} на удаление из друзей пользователя c id {}",
                userId, friendId);
        userStorage.getUser(userId).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(userId);
        log.info("deleteFriend() - выполнен запрос от пользователя с id {} на удаление из друзей пользователя c id {}",
                userId, friendId);
    }

    public Collection<User> getFriends(Long userId) {
        return userStorage.getUser(userId).getFriends().stream()
                .map(userStorage::getUser)
                .toList();
    }

    public Collection<User> getMutualFriends(Long userId, Long otherId) {
        log.info("getMutualFriends() - получен запрос от пользователя с id {} на получение общих друзей c " +
                "пользователем с id {}", userId, otherId);
        return userStorage.getUser(userId).getFriends().stream()
                .filter(id -> userStorage.getUser(otherId).getFriends().contains(id))
                .map(userStorage::getUser)
                .toList();
    }
}
