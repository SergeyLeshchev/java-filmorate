package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long idCounter = 1;

    // добавление фильма
    public Film addFilm(Film film) {
        log.info("addFilm() - получен запрос на добавление фильма {}", film);
        FilmValidator.validateFilm(film);
        film.setId(idCounter++);
        films.put(film.getId(), film);
        log.info("addFilm() - новый фильм {} успешно добавлен", film);
        return film;
    }

    // обновление фильма
    public Film updateFilm(Film film) {
        log.info("updateFilm() - получен запрос на обновление данных фильма {}", film);
        FilmValidator.validateFilm(film);
        if (!films.containsKey(film.getId())) {
            log.warn("updateFilm() - Исключение NotFoundException. Фильм с id \"{}\" не найден", film.getId());
            throw new NotFoundException("Фильм с таким id не найден");
        }
        films.put(film.getId(), film);
        log.info("updateFilm() - Данные фильма {} успешно обновлены", film);
        return film;
    }

    // получение всех фильмов
    public Collection<Film> getFilms() {
        log.info("getFilms() - получен запрос на получение всех фильмов");
        return films.values();
    }

    public Film getFilm(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Нет фильма с таким id");
        }
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Нет фильма с таким id");
        }
        return film;
    }
}
