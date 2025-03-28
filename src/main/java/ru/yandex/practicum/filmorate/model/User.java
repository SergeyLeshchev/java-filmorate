package ru.yandex.practicum.filmorate.model;

//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.Past;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class User {
    /*
     *
     * Комментарий, почему я закомментировал аннотации над полями, написан в классе Film
     *
     */
    private Long id;
    //@Email
    private String email;
    //@NotBlank
    private String login;

    private String name;
    //@NotNull
    //@Past
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public User(Long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
        // По какой-то причине во время создания объекта это поле остается null. Думаю это потому что @RequestBody
        // использует конструктор без параметров. Я решил вынести инициализацию в начало класса вместо переопределения
        // конструктора, думаю так надежнее. Не знаю как лучше
        // friends = new HashSet<>();
    }
}
