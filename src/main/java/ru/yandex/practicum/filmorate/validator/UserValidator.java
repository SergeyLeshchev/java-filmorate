package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Slf4j
public class UserValidator {
    public static void validateUser(User user) {
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
