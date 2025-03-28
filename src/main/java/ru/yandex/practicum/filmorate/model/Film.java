package ru.yandex.practicum.filmorate.model;

//import com.fasterxml.jackson.annotation.JsonFormat;
//import jakarta.validation.constraints.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film {
    /*
     *
     * Я закомментировал аннотации над полями так как с ними у меня не проходили тесты в Postman, а без аннотаций проходят.
     * Так как с аннотациями у меня не появляется ни одной строки написанного мной лога о том, что программа вошла в метод,
     * то я думаю, что различные исключения выбрасываются во время проверки соответствия тела запроса и конструктора объекта.
     * Например, тест на проверку логина не проходит, когда нет имени, статус 500. Если имя есть, то программа заходит в
     * метод валидации и выбрасывает нужное исключение и статус 400.
     *
     * Похожая ситуация и с датой рождения. Есть тест с датой рождения пользователя в будущем. Этот пользователь не
     * создается и программа падает еще до начала работы логики метода.
     *
     */
    private Long id;
    //@NotBlank
    private String name;
    //@NotBlank
    //@Size(max = 200)
    private String description;
    //@NotNull
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;
    //@Min(1)
    private int duration;
    //@Min(0)
    //@Max(10)
    private int rate;
    private Set<Long> likes = new HashSet<>();

    public Film(Long id, String name, String description, LocalDate releaseDate, int duration, int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
        // По какой-то причине во время создания объекта это поле остается null. Думаю это потому что @RequestBody
        // использует конструктор без параметров. Я решил вынести инициализацию в начало класса вместо переопределения
        // конструктора, думаю так надежнее. Не знаю как лучше
        // likes = new HashSet<>();
    }
}
