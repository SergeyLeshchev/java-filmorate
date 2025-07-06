package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.GenreStorage;
import ru.yandex.practicum.filmorate.dal.storage.LikesStorage;
import ru.yandex.practicum.filmorate.dto.film.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.film.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dal.storage.FilmStorage;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final LikesStorage likesStorage;

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        genreStorage.setGenresOfListFilms(films);
        likesStorage.setLikesOfListFilms(films);
        return films;
    }

    public Film getFilmById(long filmId) {
        Film film = filmStorage.getFilmById(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с таким id не найден");
        }
        film.setGenres(genreStorage.getFilmGenres(filmId));
        film.setLikes(likesStorage.getFilmLikes(film.getId()));
        return film;
    }

    public Film addFilm(NewFilmRequest request) {
        Film film = FilmMapper.mapToFilm(request);
        filmStorage.addFilm(film);
        return film;
    }

    public Film updateFilm(UpdateFilmRequest request) {
        Film film = filmStorage.getFilmById(request.getId());
        if (film == null) {
            throw new NotFoundException("Фильм с таким id не найден");
        }
        FilmValidator.validateFilm(film);
        return filmStorage.updateFilm(FilmMapper.updateFilmFields(film, request));
    }

    public void addLike(long filmId, long userId) {
        log.info("addLike() - получен запрос от пользователя с id {} на добавление лайка фильму с id {}",
                userId, filmId);
        filmStorage.addLike(filmId, userId);
        log.info("addLike() - выполнен запрос от пользователя с id {} на добавление лайка фильму с id {}",
                userId, filmId);
    }

    public void deleteLike(long filmId, long userId) {
        log.info("deleteLike() - получен запрос от пользователя с id {} на удаление лайка у фильма с id {}",
                userId, filmId);
        filmStorage.deleteLike(filmId, userId);
        log.info("deleteLike() - выполнен запрос от пользователя с id {} на удаление лайка у фильма с id {}",
                userId, filmId);
    }

    public List<Film> getPopularFilms(int count) {
        log.info("getPopularFilms() - получен запрос на получение популярных фильмов");
        List<Film> films = filmStorage.getPopularFilms(count).stream()
                // сортируем по убыванию лайков
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .toList();
        genreStorage.setGenresOfListFilms(films);
        likesStorage.setLikesOfListFilms(films);
        return films;
    }
}
