package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {
    private Film film;

    @BeforeEach
    void generateData() {
        film = new Film(1L, "Name", "Description", LocalDate.parse("2000-01-01"),
                90, 5, new Mpa(), new HashSet<>());
    }

    @Test
    void validateRightFilmTest() {
        assertDoesNotThrow(() -> FilmValidator.validateFilm(film),
                "Валидация не пропустила фильм с правильными полями");
    }

    @Test
    void validateFilmNameTest() {
        film.setName(null);
        assertThrows(ValidationException.class, () -> FilmValidator.validateFilm(film),
                "Валидация пропустила null в название фильма");

        film.setName("");
        assertThrows(ValidationException.class, () -> FilmValidator.validateFilm(film),
                "Валидация пропустила пустую строку в название фильма");

        film.setName(" ");
        assertThrows(ValidationException.class, () -> FilmValidator.validateFilm(film),
                "Валидация пропустила название фильма из одного пробела");
    }

    @Test
    void validateFilmDescriptionTest() {
        film.setDescription("В описании этого фильма больше 200 символов" +
                "В описании этого фильма больше 200 символов" +
                "В описании этого фильма больше 200 символов" +
                "В описании этого фильма больше 200 символов" +
                "В описании этого фильма больше 200 символов");
        assertThrows(ValidationException.class, () -> FilmValidator.validateFilm(film),
                "Валидация пропустила описание фильма длиннее 200 символов");
    }

    @Test
    void validateFilmReleaseDateTest() {
        // 28 декабря 1895 года считается днём рождения кино
        final LocalDate BIRTHDAY_FILM = LocalDate.parse("1895-12-28");
        film.setReleaseDate(BIRTHDAY_FILM.minusDays(1));
        assertThrows(ValidationException.class, () -> FilmValidator.validateFilm(film),
                "Валидация пропустила дату создания фильма раньше 1895-12-28");
    }

    @Test
    void validateFilmDurationTest() {
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> FilmValidator.validateFilm(film),
                "Валидация пропустила неположительную продолжительность фильма");
    }

    @Test
    void validateFilmRateTest() {
        film.setRate(-1);
        assertThrows(ValidationException.class, () -> FilmValidator.validateFilm(film),
                "Валидация пропустила оценку фильма меньше 0 баллов");

        film.setRate(11);
        assertThrows(ValidationException.class, () -> FilmValidator.validateFilm(film),
                "Валидация пропустила оценку фильма больше 10 баллов");
    }
}
