package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void generateData() {

        filmController = new FilmController(new InMemoryFilmStorage(), new FilmService(new InMemoryFilmStorage(),
                new InMemoryUserStorage()), new InMemoryUserStorage());
        film = new Film(1L, "Name", "Description", LocalDate.parse("2000-01-01"),
                90, 5);
    }

    @Test
    void addFilmTest() {
        filmController.addFilm(film);
        assertEquals(film, new ArrayList<>(filmController.getFilms()).getFirst(),
                "Метод addFilm или getFilms работает неправильно");
    }

    @Test
    void updateFilmTest() {
        Film film2 = new Film(1L, "Name2", "Description2", LocalDate.parse("2002-01-01"),
                100, 3);
        filmController.addFilm(film);
        filmController.updateFilm(film2);
        assertEquals(film2, new ArrayList<>(filmController.getFilms()).getFirst(),
                "Метод updateFilm или getFilms работает неправильно");
    }

    @Test
    void validateRightFilmTest() {
        assertDoesNotThrow(() -> filmController.addFilm(film),
                "Валидация не пропустила фильм с правильными полями");
    }

    @Test
    void validateFilmNameTest() {
        film.setName(null);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film),
                "Валидация пропустила null в название фильма");

        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film),
                "Валидация пропустила пустую строку в название фильма");

        film.setName(" ");
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film),
                "Валидация пропустила название фильма из одного пробела");
    }

    @Test
    void validateFilmDescriptionTest() {
        film.setDescription("В описании этого фильма больше 200 символов" +
                "В описании этого фильма больше 200 символов" +
                "В описании этого фильма больше 200 символов" +
                "В описании этого фильма больше 200 символов" +
                "В описании этого фильма больше 200 символов");
        assertThrows(ValidationException.class, () -> filmController.addFilm(film),
                "Валидация пропустила описание фильма длиннее 200 символов");
    }

    @Test
    void validateFilmReleaseDateTest() {
        // 28 декабря 1895 года считается днём рождения кино
        final LocalDate BIRTHDAY_FILM = LocalDate.parse("1895-12-28");
        film.setReleaseDate(BIRTHDAY_FILM.minusDays(1));
        assertThrows(ValidationException.class, () -> filmController.addFilm(film),
                "Валидация пропустила дату создания фильма раньше 1895-12-28");
    }

    @Test
    void validateFilmDurationTest() {
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film),
                "Валидация пропустила неположительную продолжительность фильма");
    }

    @Test
    void validateFilmRateTest() {
        film.setRate(-1);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film),
                "Валидация пропустила оценку фильма меньше 0 баллов");

        film.setRate(11);
        assertThrows(ValidationException.class, () -> filmController.addFilm(film),
                "Валидация пропустила оценку фильма больше 10 баллов");
    }
}
