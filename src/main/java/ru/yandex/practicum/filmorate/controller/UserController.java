package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Long, User> users = new HashMap<>();
    private long id = 1;

    // создание пользователя
    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("addUser() - получен запрос на добавление пользователя {}", user);
        validateUser(user);
        user.setId(id++);
        users.put(user.getId(), user);
        log.info("addUser() - новый пользователь {} успешно добавлен", user);
        return user;
    }

    // обновление пользователя
    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("updateUser() - получен запрос на обновление данных пользователя {}", user);
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            log.warn("updateUser() - Исключение NotFoundException. Пользователь с id \"{}\" не найден", user.getId());
            throw new NotFoundException("Пользователь с таким id не найден");
        }
        users.put(user.getId(), user);
        log.info("updateUser() - Данные пользователя {} успешно обновлены", user);
        return user;
    }

    // получение списка всех пользователей
    @GetMapping
    public Collection<User> getUsers() {
        log.info("getFilms() - получен запрос на получение всех пользователей");
        return users.values();
    }

    public void validateUser(User user) {
        log.info("Валидация пользователя {}", user);
        // электронная почта не может быть пустой и должна содержать символ @
        if (user.getEmail() == null || user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            log.warn("Исключение ValidationException. Электронная почта не может быть пустой и должна содержать " +
                    "символ @");
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        // логин не может быть пустым и содержать пробелы
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Исключение ValidationException. Логин не может быть пустым и содержать пробелы");
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        // имя для отображения может быть пустым — в таком случае будет использован логин
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("У пользователя используется логин в качестве имени");
        }
        // дата рождения не может быть в будущем
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Исключение ValidationException. Дата рождения не может быть в будущем");
            throw new ValidationException("Дата рождения не может быть в будущем");
        }
        log.info("Валидация данных пользователя {} прошла успешно", user);
    }
}
