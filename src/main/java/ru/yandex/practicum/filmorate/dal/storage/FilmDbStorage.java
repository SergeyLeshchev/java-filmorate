package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ConditionsNotMetException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {
    private static final String GET_FILMS = "SELECT f.film_id, f.name, f.description, f.release_date, f.duration, " +
            "f.rate, m.mpa_id, m.name AS mpa_name " +
            "FROM films f " +
            "JOIN mpa m ON f.mpa_id = m.mpa_id;";
    private static final String GET_FILM_BY_ID = "SELECT f.film_id, f.name, f.description, f.release_date, " +
            "f.duration, f.rate, m.mpa_id, m.name AS mpa_name " +
            "FROM films f " +
            "JOIN mpa m ON f.mpa_id = m.mpa_id " +
            "WHERE f.film_id = ?;";
    private static final String ADD_FILM = "INSERT INTO films(name, description, release_date, duration, rate, mpa_id)" +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_FILM = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
            "duration = ?, rate = ?, mpa_id = ? WHERE film_id = ?";
    private static final String DELETE_LIKE = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";
    private static final String ADD_LIKE = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?)";
    private static final String GET_POPULAR_FILMS = "SELECT f.film_id, f.name, f.description, f.release_date, " +
            "f.duration, f.rate, f.mpa_id, m.name AS mpa_name, COUNT(fl.user_id) AS likes_count " +
            "FROM films f " +
            "LEFT JOIN films_likes fl ON f.film_id = fl.film_id " +
            "JOIN mpa m ON f.mpa_id = m.mpa_id " +
            "GROUP BY f.film_id, f.name, f.description, f.release_date, f.duration, f.rate, f.mpa_id, m.name " +
            "ORDER BY likes_count DESC " +
            "LIMIT ?";
    private static final String ADD_FILM_GENRES = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> getFilms() {
        return findMany(GET_FILMS);
    }

    public Film getFilmById(long filmId) {
        return findOne(GET_FILM_BY_ID, filmId);
    }

    public Film addFilm(Film film) {
        FilmValidator.validateFilm(film);
        if (film.getMpa() == null || film.getMpa().getId() == null) {
            throw new NotFoundException("Mpa не задан");
        }

        long id = insert(
                ADD_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId()
        );
        film.setId(id);
        // Если у фильма нет жанров
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            film.setGenres(new HashSet<>());
        } else {
            // Проверяем, все ли жанры валидны
            boolean notHasGenreId = film.getGenres().stream().map(Genre::getId).toList().getFirst() > 6;
            if (notHasGenreId) {
                throw new NotFoundException("Genre с таким id не найдено");
            }
            // Отправляем пачку запросов на добавление жанров
            List<Object[]> batchArgs = film.getGenres().stream()
                    .map(genre -> new Object[]{id, genre.getId()})
                    .collect(Collectors.toList());
            jdbc.batchUpdate(ADD_FILM_GENRES, batchArgs);
        }
        return film;
    }

    public Film updateFilm(Film film) {
        FilmValidator.validateFilm(film);
        if (film.getMpa() == null || film.getMpa().getId() == null) {
            throw new ConditionsNotMetException("Mpa не задан");
        }
        if (film.getGenres() == null) {
            film.setGenres(new HashSet<>());
        }
        update(
                UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId()
        );
        // Отправляем пачку запросов на добавление жанров
        List<Object[]> batchArgs = film.getGenres().stream()
                .map(genre -> new Object[]{film.getId(), genre.getId()})
                .collect(Collectors.toList());
        jdbc.batchUpdate(ADD_FILM_GENRES, batchArgs);
        return film;
    }

    public void addLike(long filmId, long userId) {
        insert(ADD_LIKE, filmId, userId);
    }

    public void deleteLike(long filmId, long userId) {
        update(DELETE_LIKE, filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        return findMany(GET_POPULAR_FILMS, count);
    }
}
