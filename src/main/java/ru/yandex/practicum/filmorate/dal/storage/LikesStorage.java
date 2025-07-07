package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class LikesStorage {
    private final JdbcTemplate jdbc;
    private static final String GET_FILM_LIKES = "SELECT user_id FROM films_likes WHERE film_id = ?";

    @Autowired
    public LikesStorage(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Set<Long> getFilmLikes(long filmId) {
        return new HashSet<>(jdbc.query(
                GET_FILM_LIKES,
                // Маппинг результата SQL-запроса реализован в лямбда-выражении,
                // которое преобразует каждую строку ResultSet в объект Long (user_id).
                (rs, rowNum) -> rs.getLong("user_id"),
                filmId
        ));
    }

    public void setLikesOfListFilms(List<Film> films) {
        if (films == null || films.isEmpty()) {
            throw new NotFoundException("Нельзя получить список лайков для пустого списка фильмов");
        }
        // Получаем список id фильмов
        List<Long> filmIds = films.stream()
                .map(Film::getId)
                .toList();

        // Формируем плейсхолдеры для одного SQL-запроса к БД
        String placeholders = filmIds.stream()
                .map(id -> "?")
                .collect(Collectors.joining(", "));

        String sql = "SELECT fl.film_id, fl.user_id " +
                "FROM films_likes fl " +
                "WHERE fl.film_id IN (" + placeholders + ")";

        Object[] params = filmIds.toArray();

        // Получаем все лайки по фильмам
        List<Map<String, Object>> rows = jdbc.queryForList(sql, params);

        // Группируем лайки по film_id
        Map<Long, Set<Long>> likesByFilmId = new HashMap<>();
        for (Long id : filmIds) {
            likesByFilmId.put(id, new HashSet<>());
        }
        for (Map<String, Object> row : rows) {
            likesByFilmId.get(((Number) row.get("film_id")).longValue())
                    .add(((Number) row.get("user_id")).longValue());
        }
        films.forEach(film -> film.setLikes(likesByFilmId.get(film.getId())));
    }
}
