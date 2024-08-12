package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Data
@AllArgsConstructor
public class Film {
    @NotNull(groups = Marker.OnUpdate.class)
    private Integer id;
    private String name;
    @NotBlank @Size(max = 200)
    private String description;
    private Calendar releaseDate;
    @DurationMin(nanos = 0)
    @DurationUnit(ChronoUnit.MINUTES)
    private Duration duration;

    public static final Calendar FILM_BIRTHDAY = new GregorianCalendar(1895, Calendar.DECEMBER, 28);

    @AssertFalse(message = "Film release date is invalid")
    public boolean isValidReleaseDate() {
        return releaseDate.before(FILM_BIRTHDAY);
    }

}
