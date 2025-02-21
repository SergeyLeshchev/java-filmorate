package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
@NoArgsConstructor
public class Film {
    private Long id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @NonNull
    private Duration duration;

    public Film(Long id,
                @NonNull String name,
                @NonNull String description,
                @NonNull LocalDate releaseDate,
                @NonNull Duration duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
