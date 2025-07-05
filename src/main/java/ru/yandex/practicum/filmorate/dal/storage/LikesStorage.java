package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

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
}
