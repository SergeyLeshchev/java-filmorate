package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public String addFriend(Long userId, Long friendId) {
        log.info("addFriend() - получен запрос от пользователя с id {} на добавление в друзья пользователя c id {}",
                userId, friendId);
        if (!userStorage.getUsersMap().containsKey(userId) || !userStorage.getUsersMap().containsKey(friendId)) {
            throw new NotFoundException("Не найден пользователь с таким id");
        }
        userStorage.getUser(userId).getFriends().add(friendId);
        userStorage.getUser(friendId).getFriends().add(userId);
        log.info("addFriend() - выполнен запрос от пользователя с id {} на добавление в друзья пользователя c id {}",
                userId, friendId);
        return ""; //"Вы добавили в друзья пользователя " + userStorage.getUser(friendId).getName();
    }

    public String deleteFriend(Long userId, Long friendId) {
        log.info("deleteFriend() - получен запрос от пользователя с id {} на удаление из друзей пользователя c id {}",
                userId, friendId);
        if (!userStorage.getUsersMap().containsKey(userId) || !userStorage.getUsersMap().containsKey(friendId)) {
            throw new NotFoundException("Не найден пользователь с таким id");
        }
        userStorage.getUser(userId).getFriends().remove(friendId);
        userStorage.getUser(friendId).getFriends().remove(userId);
        log.info("deleteFriend() - выполнен запрос от пользователя с id {} на удаление из друзей пользователя c id {}",
                userId, friendId);
        return ""; //"Вы удалили из друзей пользователя " + userStorage.getUser(friendId).getName();
    }

    public Collection<User> getFriends(Long userId) {
        if (!userStorage.getUsersMap().containsKey(userId)) {
            throw new NotFoundException("Не найден пользователь с таким id");
        }
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
