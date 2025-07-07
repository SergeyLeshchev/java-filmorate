package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dal.mappers.*;
import ru.yandex.practicum.filmorate.dal.storage.*;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.dto.user.NewUserRequest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmDbStorage.class, FilmService.class, FilmController.class, FilmRowMapper.class, MpaStorage.class,
        GenreStorage.class, MpaRowMapper.class, GenreRowMapper.class, UserController.class, UserDbStorage.class,
        UserService.class, UserController.class, UserRowMapper.class, LikesStorage.class,})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmControllerTest {
    private final FilmController filmController;
    private final UserController userController;
    private Film film;
    private NewFilmRequest newFilm;
    private UpdateFilmRequest updateFilm;
    private NewUserRequest newUser;
    private final Mpa mpa = new Mpa(1L, "G");

    @BeforeEach
    void generateData() {
        film = new Film(1L, "Name1", "Description1", LocalDate.parse("2001-01-01"),
                101, 1, mpa, new HashSet<>());
        newFilm = new NewFilmRequest("Name2", "Description2", LocalDate.parse("2002-02-02"),
                102, 2, mpa, new HashSet<>());
        updateFilm = new UpdateFilmRequest(1L, "Name3", "Description3", LocalDate.parse("2003-03-03"),
                103, 3, mpa, new HashSet<>());
        newUser = new NewUserRequest("2email@gmail.com", "Login2", "Name2",
                LocalDate.parse("2002-02-02"), new HashSet<>());
    }

    @Test
    void addFilmTest() {
        filmController.addFilm(newFilm);
        film = filmController.getFilmById(1);
        assertEquals(newFilm.getName(), film.getName(), "Метод addFilm неправильно сохранил name");
        assertEquals(newFilm.getDescription(), film.getDescription(), "Метод addFilm неправильно сохранил description");
        assertEquals(newFilm.getReleaseDate(), film.getReleaseDate(), "Метод addFilm неправильно сохранил releaseDate");
        assertEquals(newFilm.getDuration(), film.getDuration(), "Метод addFilm неправильно сохранил duration");
        assertEquals(newFilm.getRate(), film.getRate(), "Метод addFilm неправильно сохранил rate");
        assertEquals(newFilm.getMpa(), film.getMpa(), "Метод addFilm неправильно сохранил mpa");
        assertEquals(newFilm.getGenres(), film.getGenres(), "Метод addFilm неправильно сохранил genres");
    }

    @Test
    void updateFilmTest() {
        filmController.addFilm(newFilm);
        filmController.updateFilm(updateFilm);
        film = filmController.getFilmById(1);
        assertEquals(updateFilm.getName(), film.getName(), "Метод addFilm неправильно сохранил name");
        assertEquals(updateFilm.getDescription(), film.getDescription(), "Метод addFilm неправильно сохранил description");
        assertEquals(updateFilm.getReleaseDate(), film.getReleaseDate(), "Метод addFilm неправильно сохранил releaseDate");
        assertEquals(updateFilm.getDuration(), film.getDuration(), "Метод addFilm неправильно сохранил duration");
        assertEquals(updateFilm.getRate(), film.getRate(), "Метод addFilm неправильно сохранил rate");
        assertEquals(updateFilm.getMpa(), film.getMpa(), "Метод addFilm неправильно сохранил mpa");
        assertEquals(updateFilm.getGenres(), film.getGenres(), "Метод addFilm неправильно сохранил genres");
    }

    @Test
    public void getFilmsTest() {
        filmController.addFilm(newFilm);
        filmController.addFilm(newFilm);
        List<Film> films = List.of(filmController.getFilmById(1), filmController.getFilmById(2));
        assertEquals(films, filmController.getFilms(), "Метод getFilms работает неправильно");
    }

    @Test
    public void getFilmByIdTest() {
        filmController.addFilm(newFilm);
        film = filmController.getFilmById(1);
        assertEquals(film, filmController.getFilmById(1), "Метод getFilmById работает неправильно");
    }

    @Test
    public void addLikeTest() {
        userController.addUser(newUser);
        filmController.addFilm(newFilm);
        filmController.addLike(1, 1);
        assertEquals(Set.of(1L), filmController.getFilmById(1).getLikes(),
                "Метод addLike не добавляет лайк фильму");
    }

    @Test
    public void deleteLikeTest() {
        userController.addUser(newUser);
        filmController.addFilm(newFilm);
        filmController.addLike(1, 1);
        filmController.deleteLike(1, 1);
        assertEquals(new HashSet<>(), filmController.getFilmById(1).getLikes(),
                "Метод deleteLike не удаляет лайк у фильма");
    }

    @Test
    public void getPopularFilmsTest() {
        userController.addUser(newUser);
        newUser.setEmail("3email@gmail.com");
        userController.addUser(newUser);
        newUser.setEmail("4email@gmail.com");
        userController.addUser(newUser);
        filmController.addFilm(newFilm);
        filmController.addFilm(newFilm);
        filmController.addFilm(newFilm);
        // Фильмы должны идти в порядке по id 3, 1, 2
        filmController.addLike(1, 1);
        filmController.addLike(2, 1);
        filmController.addLike(3, 1);
        filmController.addLike(1, 2);
        filmController.addLike(3, 2);
        filmController.addLike(3, 3);
        List<Film> films = List.of(filmController.getFilmById(3),
                filmController.getFilmById(1),
                filmController.getFilmById(2));
        assertEquals(films, filmController.getPopularFilms(10),
                "Метод getPopularFilms работает неправильно");
    }
}
