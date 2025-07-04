package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class GenreStorage extends BaseStorage<Genre> {
    private static final String GET_GENRES = "SELECT * FROM genres ORDER BY genre_id";
    private static final String GET_GENRE_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";

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
        String sql = "SELECT g.genre_id, g.name FROM films_genres fg JOIN genres g ON fg.genre_id = g.genre_id WHERE fg.film_id = ? ORDER BY genre_id ASC";
        List<Genre> genres = jdbc.query(sql, mapper, filmId);
        return new HashSet<>(genres);
    }
}
