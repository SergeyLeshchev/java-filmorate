package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.annotation.PositiveDuration;

import java.time.Duration;

public class PositiveDurationValidator implements ConstraintValidator<PositiveDuration, Duration> {

    @Override
    public void initialize(PositiveDuration constraintAnnotation) {

    }

    @Override
    public boolean isValid(Duration value, ConstraintValidatorContext context) {
        return value != null && value.isPositive();
    }
}