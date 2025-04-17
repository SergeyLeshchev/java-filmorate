package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public void addLike(Long filmId, Long userId) {
        log.info("addLike() - получен запрос от пользователя с id {} на добавление лайка фильму с id {}",
                userId, filmId);
        userStorage.getUser(userId); // для проверки, что id содержится в мапе и значение не равно null
        filmStorage.getFilm(filmId).getLikes().add(userId);
        log.info("addLike() - выполнен запрос от пользователя с id {} на добавление лайка фильму с id {}",
                userId, filmId);
    }

    public void deleteLike(Long filmId, Long userId) {
        log.info("deleteLike() - получен запрос от пользователя с id {} на удаление лайка у фильма с id {}",
                userId, filmId);
        userStorage.getUser(userId); // для проверки, что id содержится в мапе и значение не равно null
        filmStorage.getFilm(filmId).getLikes().remove(userId);
        log.info("deleteLike() - выполнен запрос от пользователя с id {} на удаление лайка у фильма с id {}",
                userId, filmId);
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("getPopularFilms() - получен запрос на получение популярных фильмов");
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
