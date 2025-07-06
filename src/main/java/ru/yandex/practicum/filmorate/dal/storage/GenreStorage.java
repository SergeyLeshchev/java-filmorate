package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class GenreStorage extends BaseStorage<Genre> {
    private static final GenreRowMapper genreRowMapper = new GenreRowMapper();
    private static final String GET_GENRES = "SELECT * FROM genres ORDER BY genre_id";
    private static final String GET_GENRE_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String GET_FILM_GENRES = "SELECT g.genre_id, g.name FROM films_genres fg " +
            "JOIN genres g ON fg.genre_id = g.genre_id WHERE fg.film_id = ? ORDER BY genre_id ASC";

    public GenreStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public List<Genre> getGenres() {
        return findMany(GET_GENRES);
    }

    public Genre getGenreById(long genreId) {
        return findOne(GET_GENRE_BY_ID, genreId);
    }

    public Set<Genre> getFilmGenres(long filmId) {
        List<Genre> genres = jdbc.query(GET_FILM_GENRES, mapper, filmId);
        return new HashSet<>(genres);
    }

    public void setGenresOfListFilms(List<Film> films) {
        if (films == null || films.isEmpty()) {
            throw new NotFoundException("Нельзя получить список жанров для пустого списка фильмов");
        }
        // Получаем список id фильмов
        List<Long> filmIds = films.stream()
                .map(Film::getId)
                .toList();

        // Формируем плейсхолдеры для одного SQL-запроса к БД
        String placeholders = filmIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        String sql = "SELECT fg.film_id, g.genre_id, g.name " +
                "FROM films_genres fg " +
                "JOIN genres g ON fg.genre_id = g.genre_id " +
                "WHERE fg.film_id IN (" + placeholders + ")";

        Object[] params = filmIds.toArray();

        // Получаем все жанры по фильмам
        List<Map<String, Object>> rows = jdbc.queryForList(sql, params);

        // Группируем жанры по film_id
        Map<Long, Set<Genre>> genresByFilmId = new HashMap<>();
        for (Long id : filmIds) {
            genresByFilmId.put(id, new HashSet<>());
        }
        for (Map<String, Object> row : rows) {
            Long id = ((Number) row.get("film_id")).longValue();
            genresByFilmId.get(id).add(genreRowMapper.mapFromMap(row));
        }
        films.forEach(film -> film.setGenres(genresByFilmId.get(film.getId())));
    }
}