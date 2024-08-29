package ru.yandex.practicum.filmorate.storage.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

@Component
public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        Date date = resultSet.getDate("releaseDate");
        if (date != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            film.setReleaseDate(cal);
        } else {
            film.setReleaseDate(null);
        }
        film.setDuration(resultSet.getInt("duration"));
        Integer ratingId = resultSet.getInt("rating_id");
        String ratingName = resultSet.getString("rating_name");
        film.setRating(new Rating(ratingId, ratingName));
        return film;
    }
}
