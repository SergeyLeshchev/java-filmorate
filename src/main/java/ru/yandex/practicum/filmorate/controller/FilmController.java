package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 1;

    // добавление фильма
    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Вход в метод добавления нового фильма");
        validateFilm(film);
        film.setId(idCounter++);
        films.put(film.getId(), film);
        log.info("Новый фильм успешно добавлен");
        return film;
    }

    // обновление фильма
    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Вход в метод обновления данных фильма");
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            log.warn("Исключение NotFoundException. Фильм с таким id не найден");
            throw new NotFoundException("Фильм с таким id не найден");
        }
        films.put(film.getId(), film);
        log.info("Данные фильма успешно обновлены");
        return film;
    }

    // получение всех фильмов
    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Вход в метод получения всех фильмов");
        return films.values();
    }

    private void validateFilm(Film film) {
        // название не может быть пустым
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Исключение ValidationException. Название не может быть пустым");
            throw new ValidationException("Название не может быть пустым");
        }

        // максимальная длина описания — 200 символов
        if (film.getDescription().length() > 200) {
            log.warn("Исключение ValidationException. Максимальная длина описания — 200 символов");
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }

        // дата релиза — не раньше 28 декабря 1895 года
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-28"))) {
            log.warn("Исключение ValidationException. Дата релиза — должна быть не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — должна быть не раньше 28 декабря 1895 года");
        }
        // продолжительность фильма должна быть положительным числом
        if (film.getDuration().isNegative()) {
            log.warn("Исключение ValidationException. Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        log.info("Валидация данных фильма прошла успешно");
    }
}
