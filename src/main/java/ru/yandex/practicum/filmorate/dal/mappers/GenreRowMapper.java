package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@Component
public class GenreRowMapper implements RowMapper<Genre> {
    @Override
    public Genre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre();
        genre.setId(resultSet.getLong("genre_id"));
        genre.setName(resultSet.getString("name"));
        return genre;
    }

    public Genre mapFromMap(Map<String, Object> row) {
        return new Genre(
                ((Number) row.get("genre_id")).longValue(),
                (String) row.get("name")
        );
    }
}
