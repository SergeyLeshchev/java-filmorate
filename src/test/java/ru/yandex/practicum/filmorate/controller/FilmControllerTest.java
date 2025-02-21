package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private Film film;

    @BeforeEach
    void generateData() {
        filmController = new FilmController();
        film = new Film(1L, "Name", "Description", LocalDate.parse("2000-01-01"),
                Duration.ofSeconds(3600));
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
                Duration.ofSeconds(1800));
        filmController.addFilm(film);
        filmController.updateFilm(film2);
        assertEquals(film2, new ArrayList<>(filmController.getFilms()).getFirst(),
                "Метод updateFilm или getFilms работает неправильно");
    }

    @Test
    void validateFilmTest() {
        assertDoesNotThrow(() -> filmController.addFilm(film),
                "Валидация не пропустила фильм с правильными полями");

        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film),
                "Валидация пропустила пустую строку в название фильма");

        film.setName(" ");
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film),
                "Валидация пропустила название фильма из одного пробела");

        film.setName("Name");
        film.setDescription("В описании этого фильма больше 200 символов" +
                "В описании этого фильма больше 200 символов" +
                "В описании этого фильма больше 200 символов" +
                "В описании этого фильма больше 200 символов" +
                "В описании этого фильма больше 200 символов");
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film),
                "Валидация пропустила описание фильма длиннее 200 символов");

        film.setDescription("Description");
        film.setReleaseDate(LocalDate.parse("1895-12-27"));
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film),
                "Валидация пропустила дату создания фильма раньше 1895-12-28");

        film.setReleaseDate(LocalDate.parse("2000-01-01"));
        film.setDuration(Duration.ofSeconds(-1));
        assertThrows(ValidationException.class, () -> filmController.updateFilm(film),
                "Валидация пропустила отрицательную продолжительность фильма");
    }
}
