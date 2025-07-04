package ru.yandex.practicum.filmorate.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private Set<Long> friends;
}
