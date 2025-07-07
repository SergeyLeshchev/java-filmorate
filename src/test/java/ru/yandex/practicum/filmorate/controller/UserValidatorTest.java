package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserValidatorTest {
    private User user;

    @BeforeEach
    void generateData() {
        user = new User(1L, "1email@gmail.com", "Login1", "Name1",
                LocalDate.parse("2001-01-01"));
    }

    @Test
    void validateRightUserTest() {
        assertDoesNotThrow(() -> UserValidator.validateUser(user),
                "Валидация не пропустила пользователя с правильными полями");
    }

    @Test
    void validateUserEmailTest() {
        user.setEmail(null);
        assertThrows(ValidationException.class, () -> UserValidator.validateUser(user),
                "Валидация пропустила null в email");

        user.setEmail("");
        assertThrows(ValidationException.class, () -> UserValidator.validateUser(user),
                "Валидация пропустила пустую строку в email");

        user.setEmail(" ");
        assertThrows(ValidationException.class, () -> UserValidator.validateUser(user),
                "Валидация пропустила email из одного пробела");

        user.setEmail("email-2gmail.com");
        assertThrows(ValidationException.class, () -> UserValidator.validateUser(user),
                "Валидация пропустила email пользователя без символа @");
    }

    @Test
    void validateUserLoginTest() {
        user.setLogin(null);
        assertThrows(ValidationException.class, () -> UserValidator.validateUser(user),
                "Валидация пропустила null в логин");

        user.setLogin("");
        assertThrows(ValidationException.class, () -> UserValidator.validateUser(user),
                "Валидация пропустила пустую строку в логин");

        user.setLogin(" ");
        assertThrows(ValidationException.class, () -> UserValidator.validateUser(user),
                "Валидация пропустила логин из одного пробела");

        user.setLogin("Login login");
        assertThrows(ValidationException.class, () -> UserValidator.validateUser(user),
                "Валидация пропустила логин пользователя с символом пробела");
    }

    @Test
    void validateUserNameTest() {
        user.setName(null);
        UserValidator.validateUser(user);
        assertEquals(user.getLogin(), user.getName(),
                "Валидация не присвоила логин имени пользователя из null");

        user.setName("");
        UserValidator.validateUser(user);
        assertEquals(user.getLogin(), user.getName(),
                "Валидация не присвоила логин имени пользователя из пустой строки");

        user.setName(" ");
        UserValidator.validateUser(user);
        assertEquals(user.getLogin(), user.getName(),
                "Валидация не присвоила логин имени пользователя из одного пробела");
    }

    @Test
    void validateUserBirthdayTest() {
        user.setBirthday(LocalDate.now().plus(Period.ofDays(1)));
        assertThrows(ValidationException.class, () -> UserValidator.validateUser(user),
                "Валидация пропустила дату рождения пользователя в будущем");
    }
}
