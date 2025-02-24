package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
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
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("addFilm() - получен запрос на добавление фильма {}", film);
        validateFilm(film);
        film.setId(idCounter++);
        films.put(film.getId(), film);
        log.info("addFilm() - новый фильм {} успешно добавлен", film);
        return film;
    }

    // обновление фильма
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("updateFilm() - получен запрос на обновление данных фильма {}", film);
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            log.warn("updateFilm() - Исключение NotFoundException. Фильм с id \"{}\" не найден", film.getId());
            throw new NotFoundException("Фильм с таким id не найден");
        }
        films.put(film.getId(), film);
        log.info("updateFilm() - Данные фильма {} успешно обновлены", film);
        return film;
    }

    // получение всех фильмов
    @GetMapping
    public Collection<Film> getFilms() {
        log.info("getFilms() - получен запрос на получение всех фильмов");
        return films.values();
    }

    private void validateFilm(Film film) {
        log.info("Валидация фильма {}", film);
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
        // 28 декабря 1895 года считается днём рождения кино
        final LocalDate BIRTHDAY_FILM = LocalDate.parse("1895-12-28");
        if (film.getReleaseDate().isBefore(BIRTHDAY_FILM)) {
            log.warn("Исключение ValidationException. Дата релиза — должна быть не раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза — должна быть не раньше 28 декабря 1895 года");
        }

        // продолжительность фильма должна быть положительным числом
        if (film.getDuration().isNegative()) {
            log.warn("Исключение ValidationException. Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }
        log.info("Валидация данных фильма {} прошла успешно", film);
    }
}
