package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.dal.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.dto.user.UpdateUserRequest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserService.class, UserController.class, UserRowMapper.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTest {
    @Autowired
    private UserController userController;
    private User user;
    private NewUserRequest newUser;
    private UpdateUserRequest updateUser;

    @BeforeEach
    void generateData() {
        user = new User(1L, "1email@gmail.com", "Login1", "Name1",
                LocalDate.parse("2001-01-01"));
        newUser = new NewUserRequest("2email@gmail.com", "Login2", "Name2",
                LocalDate.parse("2002-02-02"), new HashSet<>());
        updateUser = new UpdateUserRequest(1L, "3email@gmail.com", "Login3", "Name3",
                LocalDate.parse("2003-03-03"), new HashSet<>());
    }

    @Test
    void addUserTest() {
        userController.addUser(newUser);
        user = userController.getUserById(1);
        assertEquals(newUser.getEmail(), user.getEmail(), "Метод addUser неправильно сохранил email");
        assertEquals(newUser.getLogin(), user.getLogin(), "Метод addUser неправильно сохранил login");
        assertEquals(newUser.getName(), user.getName(), "Метод addUser неправильно сохранил name");
        assertEquals(newUser.getBirthday(), user.getBirthday(), "Метод addUser неправильно сохранил birthday");
        assertEquals(newUser.getFriends(), user.getFriends(), "Метод addUser неправильно сохранил friends");
    }

    @Test
    void updateUserTest() {
        userController.addUser(newUser);
        userController.updateUser(updateUser);
        user = userController.getUserById(1);
        assertEquals(user.getEmail(), updateUser.getEmail(), "Метод updateUser неправильно сохранил email");
        assertEquals(user.getLogin(), updateUser.getLogin(), "Метод updateUser неправильно сохранил login");
        assertEquals(user.getName(), updateUser.getName(), "Метод updateUser неправильно сохранил name");
        assertEquals(user.getBirthday(), updateUser.getBirthday(), "Метод updateUser неправильно сохранил birthday");
        assertEquals(user.getFriends(), updateUser.getFriends(), "Метод updateUser неправильно сохранил friends");
    }

    @Test
    public void getUsersTest() {
        userController.addUser(newUser);
        newUser.setEmail("3email@gmail.com");
        userController.addUser(newUser);
        List<User> users = List.of(userController.getUserById(1), userController.getUserById(2));
        assertEquals(users, userController.getUsers(), "Метод getUsers работает неправильно");
    }

    @Test
    public void getUserByIdTest() {
        userController.addUser(newUser);
        user = userController.getUserById(1);
        assertEquals(user, userController.getUserById(1), "Метод getUserById работает неправильно");
    }

    @Test
    public void addFriendTest() {
        userController.addUser(newUser);
        newUser.setEmail("3email@gmail.com");
        userController.addUser(newUser);
        userController.addFriend(1, 2);
        assertEquals(new HashSet<>(), userController.getUserById(2).getFriends(),
                "Метод addFriend добавляет пользователя в друзья друга");
        assertEquals(Set.of(2L), userController.getUserById(1).getFriends(),
                "Метод addFriend не добавляет друга");
    }

    @Test
    public void deleteFriendTest() {
        userController.addUser(newUser);
        newUser.setEmail("3email@gmail.com");
        userController.addUser(newUser);
        userController.addFriend(1, 2);
        userController.deleteFriend(1, 2);
        assertEquals(new HashSet<>(), userController.getUserById(2).getFriends(),
                "Метод deleteFriend не удаляет друга у пользователя");
    }

    @Test
    public void getFriendsTest() {
        userController.addUser(newUser);
        newUser.setEmail("3email@gmail.com");
        userController.addUser(newUser);
        userController.addFriend(1, 2);
        assertEquals(Set.of(2L), userController.getUserById(1).getFriends(),
                "Метод getFriend работает неправильно");
    }

    @Test
    public void getMutualFriendsTest() {
        userController.addUser(newUser);
        newUser.setEmail("3email@gmail.com");
        userController.addUser(newUser);
        newUser.setEmail("4email@gmail.com");
        userController.addUser(newUser);
        userController.addFriend(1, 3);
        userController.addFriend(2, 3);
        assertEquals(List.of(userController.getUserById(3)), userController.getMutualFriends(1, 2),
                "Метод getMutualFriends работает неправильно");
    }
}
