package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
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
    private Mpa mpa;
    private List<Genre> genres;
    private Set<Integer> likes = new HashSet<>();

    public static final Calendar FILM_BIRTHDAY = new GregorianCalendar(1895, Calendar.DECEMBER, 28);

    public Film(Integer id, String name, String description, Calendar releaseDate, Integer duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
    }

    public Film(Integer id, String name, String description, Calendar releaseDate, Integer duration, Mpa mpa,
                List<Genre> genres) {
        this(id, name, description, releaseDate, duration, mpa);
        this.genres = genres;
    }

    @AssertFalse(message = "Film release date is invalid")
    public boolean isValidReleaseDate() {
        if (releaseDate == null) {
            return false;
        }
        return releaseDate.before(FILM_BIRTHDAY);
    }

    public void addLike(Integer id) {
        likes.add(id);
    }

    public void deleteLike(Integer id) {
        likes.remove(id);
    }


}
