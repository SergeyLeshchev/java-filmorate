package ru.yandex.practicum.filmorate.dal.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.storage.GenreStorage;
import ru.yandex.practicum.filmorate.dal.storage.LikesStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@RequiredArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private final LikesStorage likesStorage;
    private final GenreStorage genreStorage;

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getLong("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setReleaseDate(resultSet.getTimestamp("release_date").toLocalDateTime().toLocalDate());
        film.setDuration(resultSet.getInt("duration"));
        film.setRate(resultSet.getInt("rate"));
        film.setMpa(new Mpa(
                resultSet.getLong("mpa_id"),
                resultSet.getString("mpa_name")));
        film.setGenres(genreStorage.getFilmGenres(film.getId()));
        film.setLikes(likesStorage.getFilmLikes(film.getId()));
        return film;
    }
}
