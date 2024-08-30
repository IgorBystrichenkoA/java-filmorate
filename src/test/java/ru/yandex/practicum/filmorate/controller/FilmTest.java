package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    @DisplayName("Проверка валидации пустого имени")
    void validateDescriptionNotBlankTest() {
        final Film film = new Film(1, "", "Film name blank test",
                Film.FILM_BIRTHDAY, 10, new Mpa("Комедия"));
        assertValidation(film, NotBlank.class, "name");
    }

    @Test
    @DisplayName("Проверка валидации длинного описания")
    void validateDescriptionLengthTest() {
        final Film film = new Film(1, "Film description is too long", "a".repeat(201),
                Film.FILM_BIRTHDAY, 10, new Mpa("Комедия"));
        assertValidation(film, Size.class, "description");
    }

    @Test
    @DisplayName("Проверка валидации неправильной даты")
    void validateReleaseDateTest() {
        final Film film = new Film(1, "Film release date is invalid", "a".repeat(200),
                new GregorianCalendar(1895, Calendar.DECEMBER, 27), 10, new Mpa("Комедия"));
        assertValidation(film, AssertFalse.class, "validReleaseDate");
    }

    @Test
    @DisplayName("Проверка валидации отрицательной длительности")
    void validateDurationTest() {
        final Film film = new Film(1, "Film duration is invalid", "a",
                Film.FILM_BIRTHDAY, -10, new Mpa("Комедия"));
        assertValidation(film, PositiveOrZero.class, "duration");
    }

    @Test
    @DisplayName("Проверка валидации наличия id при Update")
    void validateIdTest() {
        final Film film = new Film(null, "Film duration is invalid", "a",
                Film.FILM_BIRTHDAY, 10, new Mpa("Комедия"));
        assertValidation(film, NotNull.class, "id", Marker.OnUpdate.class);
    }

    void assertValidation(Film film, Class<? extends Annotation> annotationClass, String propertyName, Class<?> group) {
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
}