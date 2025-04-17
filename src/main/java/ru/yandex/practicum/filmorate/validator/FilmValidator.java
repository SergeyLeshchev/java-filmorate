package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {
    public static void validateFilm(Film film) {
        log.info("Валидация фильма {}", film);
        if (film == null) {
            log.warn("Исключение ValidationException. Объект film не может быть пустым");
            throw new ValidationException("Объект film не может быть пустым");
        }

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
        if (film.getDuration() < 1) {
            log.warn("Исключение ValidationException. Продолжительность фильма должна быть положительным числом");
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }

        // оценка фильма должна быть от 1 до 10 баллов
        if (film.getRate() < 0 || film.getRate() > 10) {
            log.warn("Исключение ValidationException. Оценка фильма должна быть от 0 до 10 баллов");
            throw new ValidationException("Оценка фильма должна быть от 0 до 10 баллов");
        }
        log.info("Валидация данных фильма {} прошла успешно", film);
    }
}
