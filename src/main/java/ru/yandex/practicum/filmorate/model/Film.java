package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private int rate;
    private Mpa mpa;
    private Set<Long> likes = new HashSet<>();
    private Set<Genre> genres = new HashSet<>();

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, int rate, Mpa mpa,
                Set<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
    }
}
