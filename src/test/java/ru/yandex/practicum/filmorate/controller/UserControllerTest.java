package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    private UserController userController;
    private User user;

    @BeforeEach
    void generateData() {
        userController = new UserController(new InMemoryUserStorage(), new UserService(new InMemoryUserStorage()));
        user = new User(1L, "email@gmail.com", "Login", "Name",
                LocalDate.parse("2000-01-01"));
    }

    @Test
    void addUserTest() {
        userController.addUser(user);
        assertEquals(user, new ArrayList<>(userController.getUsers()).getFirst(),
                "Метод addUser или getUsers работает неправильно");
    }

    @Test
    void updateUserTest() {
        User user2 = new User(1L, "email-2@gmail.com", "Login2", "Name2",
                LocalDate.parse("2002-01-01"));
        userController.addUser(user);
        userController.updateUser(user2);
        assertEquals(user2, new ArrayList<>(userController.getUsers()).getFirst(),
                "Метод updateUser или getUsers работает неправильно");
    }

    @Test
    void validateRightUserTest() {
        assertDoesNotThrow(() -> userController.addUser(user),
                "Валидация не пропустила пользователя с правильными полями");
    }

    @Test
    void validateUserEmailTest() {
        user.setEmail(null);
        assertThrows(ValidationException.class, () -> userController.addUser(user),
                "Валидация пропустила null в email");

        user.setEmail("");
        assertThrows(ValidationException.class, () -> userController.updateUser(user),
                "Валидация пропустила пустую строку в email");

        user.setEmail(" ");
        assertThrows(ValidationException.class, () -> userController.updateUser(user),
                "Валидация пропустила email из одного пробела");

        user.setEmail("email-2gmail.com");
        assertThrows(ValidationException.class, () -> userController.updateUser(user),
                "Валидация пропустила email пользователя без символа @");
    }

    @Test
    void validateUserLoginTest() {
        user.setLogin(null);
        assertThrows(ValidationException.class, () -> userController.addUser(user),
                "Валидация пропустила null в логин");

        user.setLogin("");
        assertThrows(ValidationException.class, () -> userController.updateUser(user),
                "Валидация пропустила пустую строку в логин");

        user.setLogin(" ");
        assertThrows(ValidationException.class, () -> userController.updateUser(user),
                "Валидация пропустила логин из одного пробела");

        user.setLogin("Login login");
        assertThrows(ValidationException.class, () -> userController.updateUser(user),
                "Валидация пропустила логин пользователя с символом пробела");
    }

    @Test
    void validateUserNameTest() {
        user.setName(null);
        userController.addUser(user);
        assertEquals(user.getLogin(), user.getName(),
                "Валидация не присвоила логин имени пользователя из null");

        user.setName("");
        userController.updateUser(user);
        assertEquals(user.getLogin(), user.getName(),
                "Валидация не присвоила логин имени пользователя из пустой строки");

        user.setName(" ");
        userController.updateUser(user);
        assertEquals(user.getLogin(), user.getName(),
                "Валидация не присвоила логин имени пользователя из одного пробела");
    }

    @Test
    void validateUserBirthdayTest() {
        user.setBirthday(LocalDate.now().plus(Period.ofDays(1)));
        assertThrows(ValidationException.class, () -> userController.addUser(user),
                "Валидация пропустила дату рождения пользователя в будущем");
    }
}
