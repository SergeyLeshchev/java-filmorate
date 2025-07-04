package ru.yandex.practicum.filmorate.dto.film;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewFilmRequest {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private int rate;
    private Mpa mpa;
    private Set<Genre> genres;
}
