package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {
    private static final Validator validator;
    private static final Calendar VALID_CALENDAR = new GregorianCalendar(2000, Calendar.JANUARY, 1);
    static {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    void assertValidation(User user, Class<? extends Annotation> annotationClass, String propertyName, Class<?> group) {
        Set<ConstraintViolation<User>> violations;
        if (group == null) {
            violations = validator.validate(user);
        } else {
            violations = validator.validate(user, group);
        }
        assertFalse(violations.isEmpty(), "Violation not found");
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals(annotationClass, violation.getConstraintDescriptor().getAnnotation().annotationType(),
                "Ожидаемый тип валидации не соответствует действительному");
        assertEquals(propertyName, violation.getPropertyPath().toString(),
                "Ошибка валидации неожидаемого поля");
    }

    void assertValidation(User user, Class<? extends Annotation> annotationClass, String propertyName) {
        assertValidation(user, annotationClass, propertyName, null);
    }

    @Test
    @DisplayName("Проверка валидации email")
    void validateEmailTest() {
        final User user = new User(1, "@", "login", "Invalid email address", VALID_CALENDAR);
        assertValidation(user, Email.class, "email");
    }

    @Test
    @DisplayName("Проверка валидации login на NotBlank")
    void validateLoginNullTest() {
        final User user = new User(1, "eee@eee.com", null, "Invalid login", VALID_CALENDAR);
        assertValidation(user, NotBlank.class, "login");
    }

    @Test
    @DisplayName("Проверка валидации login")
    void validateLoginWhitespaceTest() {
        final User user = new User(1, "eee@eee.com", "log in", "Invalid login", VALID_CALENDAR);
        assertValidation(user, Pattern.class, "login");
    }

    @Test
    @DisplayName("Проверка валидации birthday")
    void validateBirthdayTest() {
        Calendar futureCalendar = new GregorianCalendar();
        futureCalendar.add(Calendar.YEAR, 1);
        final User user = new User(1, "eee@eee.com", "login", "Invalid birthday", futureCalendar);
        assertValidation(user, PastOrPresent.class, "birthday");
    }

    @Test
    @DisplayName("Проверка валидации наличия id при Update")
    void validateIdTest() {
        final User user = new User(null, "eee@eee.com", "login", "Invalid login", VALID_CALENDAR);
        assertValidation(user, NotNull.class, "id", Marker.OnUpdate.class);
    }
}