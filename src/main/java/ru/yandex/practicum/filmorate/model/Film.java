package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {
    private Long id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
    @Min(0)
    @Max(10)
    private int rate;
    private Set<Long> likes = new HashSet<>();

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }
}
