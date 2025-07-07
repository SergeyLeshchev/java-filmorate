package ru.yandex.practicum.filmorate.dal.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Repository
public class MpaStorage extends BaseStorage<Mpa> {
    private static final String GET_ALL_MPA = "SELECT * FROM mpa ORDER BY mpa_id";
    private static final String GET_MPA_BY_ID = "SELECT * FROM mpa WHERE mpa_id = ?";

    public MpaStorage(JdbcTemplate jdbc, @Qualifier("mpaRowMapper") RowMapper<Mpa> mapper) {
        super(jdbc, mapper);
    }

    public List<Mpa> getAllMpa() {
        return findMany(GET_ALL_MPA);
    }

    public Mpa getMpaById(long mpaId) {
        return findOne(GET_MPA_BY_ID, mpaId);
    }
}
