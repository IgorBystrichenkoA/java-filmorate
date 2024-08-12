package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.time.DurationMin;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;

import java.lang.annotation.Annotation;
import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {
    private static final Validator validator;

    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    void assertValidation(Film film, Class<? extends Annotation> annotationClass, String propertyName,
                            Class<?> group) {
        Set<ConstraintViolation<Film>> violations;
        if (group == null) {
            violations = validator.validate(film);
        } else {
            violations = validator.validate(film, group);
        }
        assertFalse(violations.isEmpty(), "Violation not found");
        ConstraintViolation<Film> violation = violations.iterator().next();
        assertEquals(annotationClass, violation.getConstraintDescriptor().getAnnotation().annotationType(),
                "Ожидаемый тип валидации не соответствует действительному");
        assertEquals(propertyName, violation.getPropertyPath().toString(),
                "Ошибка валидации неожидаемого поля");
    }

    void assertValidation(Film film, Class<? extends Annotation> annotationClass, String propertyName) {
        assertValidation(film, annotationClass, propertyName, null);
    }

    @Test
    @DisplayName("Проверка валидации пустого описания")
    void validateDescriptionNotBlankTest() {
        final Film film = new Film(1, "Film description is blank test", "",
                Film.FILM_BIRTHDAY, Duration.ofMinutes(10));
        assertValidation(film, NotBlank.class, "description");
    }

    @Test
    @DisplayName("Проверка валидации длинного описания")
    void validateDescriptionLengthTest() {
        final Film film = new Film(1, "Film description is too long", "a".repeat(201),
                Film.FILM_BIRTHDAY, Duration.ofMinutes(10));
        assertValidation(film, Size.class, "description");
    }

    @Test
    @DisplayName("Проверка валидации неправильной даты")
    void validateReleaseDateTest() {
        final Film film = new Film(1, "Film release date is invalid", "a".repeat(200),
                new GregorianCalendar(1895, Calendar.DECEMBER, 27), Duration.ofMinutes(10));
        assertValidation(film, AssertFalse.class, "validReleaseDate");
    }

    @Test
    @DisplayName("Проверка валидации отрицательной длительности")
    void validateDurationTest() {
        final Film film = new Film(1, "Film duration is invalid", "a",
                Film.FILM_BIRTHDAY, Duration.ofMinutes(-10));
        assertValidation(film, DurationMin.class, "duration");
    }

    @Test
    @DisplayName("Проверка валидации наличия id при Update")
    void validateIdTest() {
        final Film film = new Film(null, "Film duration is invalid", "a",
                Film.FILM_BIRTHDAY, Duration.ofMinutes(10));
        assertValidation(film, NotNull.class, "id", Marker.OnUpdate.class);
    }
}