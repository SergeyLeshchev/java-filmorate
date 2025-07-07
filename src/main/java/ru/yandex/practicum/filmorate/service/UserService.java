package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.UserStorage;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return new ArrayList<>(userStorage.getUsers());
    }

    public User getUserById(long userId) {
        User user = userStorage.getUserById(userId);
        if (user == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        user.setFriends(userStorage.getFriends(userId).stream().map(User::getId).collect(Collectors.toSet()));
        return user;
    }

    public User addUser(NewUserRequest request) {
        User user = UserMapper.mapToUser(request);
        UserValidator.validateUser(user);
        user = userStorage.addUser(user);
        return user;
    }

    public User updateUser(UpdateUserRequest request) {
        User user = userStorage.getUserById(request.getId());
        if (user == null) {
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        UserValidator.validateUser(user);
        return userStorage.updateUser(UserMapper.updateUserFields(user, request));
    }

    public List<User> getFriends(Long userId) {
        Set<Long> userIds = userStorage.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        if (!userIds.contains(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        }
        return userStorage.getFriends(userId);
    }

    public void addFriend(Long userId, Long friendId) {
        log.info("addFriend() - получен запрос от пользователя с id {} на добавление в друзья пользователя c id {}",
                userId, friendId);
        // Проверка, что такие id содержатся в БД
        Set<Long> userIds = userStorage.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        if (!userIds.contains(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else if (!userIds.contains(friendId)) {
            throw new NotFoundException("Пользователь с id " + friendId + " не найден");
        }
        userStorage.addFriend(userId, friendId);
        log.info("addFriend() - выполнен запрос от пользователя с id {} на добавление в друзья пользователя c id {}",
                userId, friendId);
    }

    public void deleteFriend(Long userId, Long friendId) {
        log.info("deleteFriend() - получен запрос от пользователя с id {} на удаление из друзей пользователя c id {}",
                userId, friendId);
        Set<Long> userIds = userStorage.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toSet());
        if (!userIds.contains(userId)) {
            throw new NotFoundException("Пользователь с id " + userId + " не найден");
        } else if (!userIds.contains(friendId)) {
            throw new NotFoundException("Пользователь с id " + friendId + " не найден");
        }
        userStorage.deleteFriend(userId, friendId);
        log.info("deleteFriend() - выполнен запрос от пользователя с id {} на удаление из друзей пользователя c id {}",
                userId, friendId);
    }

    public List<User> getMutualFriends(Long userId, Long otherId) {
        log.info("getMutualFriends() - получен запрос от пользователя с id {} на получение общих друзей c " +
                "пользователем с id {}", userId, otherId);

        // Получаем множества id друзей каждого пользователя
        Set<Long> otherFriendIds = userStorage.getFriends(otherId).stream()
                .map(User::getId)
                .collect(Collectors.toSet());

        // Фильтруем друзей первого пользователя, оставляя только тех, чьи id есть среди друзей второго пользователя
        return userStorage.getFriends(userId).stream()
                .filter(u -> otherFriendIds.contains(u.getId()))
                .toList();
    }
}
