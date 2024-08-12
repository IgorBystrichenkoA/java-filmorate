package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Calendar;
import java.util.GregorianCalendar;

@Data
@AllArgsConstructor
public class Film {
    @NotNull(groups = Marker.OnUpdate.class)
    private Integer id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Calendar releaseDate;
    @PositiveOrZero
    private Integer duration;

    public static final Calendar FILM_BIRTHDAY = new GregorianCalendar(1895, Calendar.DECEMBER, 28);

    @AssertFalse(message = "Film release date is invalid")
    public boolean isValidReleaseDate() {
        return releaseDate.before(FILM_BIRTHDAY);
    }

}
