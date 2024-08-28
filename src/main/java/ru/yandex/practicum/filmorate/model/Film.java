package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Data
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
    private Set<Integer> likes = ConcurrentHashMap.newKeySet();

    public Film(Integer id, String name, String description, Calendar releaseDate, Integer duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @AssertFalse(message = "Film release date is invalid")
    public boolean isValidReleaseDate() {
        return releaseDate.before(FILM_BIRTHDAY);
    }

    public void addLike(Integer id) {
        likes.add(id);
    }

    public void deleteLike(Integer id) {
        likes.remove(id);
    }


}
