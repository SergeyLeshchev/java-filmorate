package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    // создание пользователя
    public User addUser(User user) {
        log.info("addUser() - получен запрос на добавление пользователя {}", user);
        UserValidator.validateUser(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("addUser() - новый пользователь {} успешно добавлен", user);
        return user;
    }

    // обновление пользователя
    public User updateUser(User user) {
        log.info("updateUser() - получен запрос на обновление данных пользователя {}", user);
        UserValidator.validateUser(user);
        if (!users.containsKey(user.getId())) {
            log.warn("updateUser() - Исключение NotFoundException. Пользователь с id \"{}\" не найден", user.getId());
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        users.put(user.getId(), user);
        log.info("updateUser() - Данные пользователя {} успешно обновлены", user);
        return user;
    }

    // получение списка всех пользователей
    public Collection<User> getUsers() {
        log.info("getFilms() - получен запрос на получение всех пользователей");
        return users.values();
    }

    public User getUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("Нет пользователя с таким id");
        }
        return user;
    }
}
