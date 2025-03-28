package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public String addLike(Long filmId, Long userId) {
        log.info("addLike() - получен запрос от пользователя с id {} на добавление лайка фильму с id {}",
                userId, filmId);
        if (!filmStorage.getFilmsMap().containsKey(filmId) || !userStorage.getUsersMap().containsKey(userId)) {
            throw new NotFoundException("Не найден пользователь с таким id");
        }
        filmStorage.getFilm(filmId).getLikes().add(userId);
        log.info("addLike() - выполнен запрос от пользователя с id {} на добавление лайка фильму с id {}",
                userId, filmId);
        return ""; //"Вы поставили лайк фильму " + filmStorage.getFilm(filmId).getName();
    }

    public String deleteLike(Long filmId, Long userId) {
        log.info("deleteLike() - получен запрос от пользователя с id {} на удаление лайка у фильма с id {}",
                userId, filmId);
        if (!filmStorage.getFilmsMap().containsKey(filmId) || !userStorage.getUsersMap().containsKey(userId)) {
            throw new NotFoundException("Не найден пользователь с таким id");
        }
        filmStorage.getFilm(filmId).getLikes().remove(userId);
        log.info("deleteLike() - выполнен запрос от пользователя с id {} на удаление лайка у фильма с id {}",
                userId, filmId);
        return ""; //"Вы удалили лайк у фильма " + filmStorage.getFilm(filmId).getName();
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("getPopularFilms() - получен запрос на получение популярных фильмов");
        return filmStorage.getFilms().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
