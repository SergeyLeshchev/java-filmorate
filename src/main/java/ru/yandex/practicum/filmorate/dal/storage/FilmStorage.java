package ru.yandex.practicum.filmorate.dal.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film getFilmById(long filmId);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void addLike(long filmId, long userId);

    void deleteLike(long filmId, long userId);

    List<Film> getPopularFilms(int count);
}
