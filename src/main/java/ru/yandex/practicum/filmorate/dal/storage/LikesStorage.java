package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Set;

@Repository
public class LikesStorage extends BaseStorage<Long> {
    private static final String GET_FILM_LIKES = "SELECT user_id FROM films_likes WHERE film_id = ?";

    @Autowired
    public LikesStorage(JdbcTemplate jdbc, @Qualifier("likesRowMapper") RowMapper<Long> mapper) {
        super(jdbc, mapper);
    }

    public Set<Long> getFilmLikes(long filmId) {
        return new HashSet<>(findMany(GET_FILM_LIKES, filmId));
    }
}
