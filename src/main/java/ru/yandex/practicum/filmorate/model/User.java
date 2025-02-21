package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class User {
    private Long id;
    @NonNull
    private String email;
    @NonNull
    private String login;
    private String name;
    @NonNull
    private LocalDate birthday;

    public User(Long id,
                @NonNull String email,
                @NonNull String login,
                String name,
                @NonNull LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
