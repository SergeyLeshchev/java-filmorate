package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.storage.GenreStorage;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public Genre getGenreById(long genreId) {
        if (genreStorage.getGenres().stream()
                .noneMatch(genre -> genre.getId() == genreId)) {
            throw new NotFoundException("Genre с таким id не найдено");
        }
        return genreStorage.getGenreById(genreId);
    }
}
