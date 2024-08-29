package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.rowMapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.rowMapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.rowMapper.RatingRowMapper;

import java.util.Collection;

@Component("h2FilmStorage")
public class FilmDbStorage implements FilmStorage {
    JdbcTemplate jdbcTemplate;
    FilmRowMapper filmRowMapper;
    GenreRowMapper genreRowMapper;
    RatingRowMapper ratingRowMapper;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         FilmRowMapper filmRowMapper,
                         GenreRowMapper genreRowMapper,
                         RatingRowMapper ratingRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmRowMapper = filmRowMapper;
        this.genreRowMapper = genreRowMapper;
        this.ratingRowMapper = ratingRowMapper;
    }

    @Override
    public Film create(Film film) {
        String query = "INSERT INTO films (name, description, releaseDate, duration, rating_id) VALUES (?,?,?,?,?)";
        Integer ratingId = film.getRating() == null ? null : film.getRating().getId();
        jdbcTemplate.update(query, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), ratingId);
        return film;
    }

    @Override
    public Film get(Integer id) {
        String query = "SELECT f.*, r.name AS rating_name FROM films AS f " +
                "INNER JOIN ratings AS r ON f.rating_id = r.id " +
                "WHERE id = ?";
        Film result = jdbcTemplate.queryForObject(query, filmRowMapper, id);
        if (result == null) {
            throw new NotFoundException("Film not found");
        }
        return result;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name =?, description =?, releaseDate =?, duration =?, rating_id =? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getRating() == null ? null : film.getRating().getId(), film.getId());
        return film;
    }

    @Override
    public Film delete(Film film) {
        String query = "DELETE FROM films WHERE id = ?";
        jdbcTemplate.update(query, film.getId());
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String query = "SELECT f.*, r.name AS rating_name FROM films AS f " +
                "INNER JOIN ratings AS r ON f.rating_id = r.id";
        return jdbcTemplate.query(query, filmRowMapper);
    }

    @Override
    public Collection<Film> getTopFilms(Integer count) {
        String query = "SELECT f.*, r.name FROM films AS f " +
                "INNER JOIN ratings AS r ON f.rating_id = r.id" +
                "ORDER BY likes DESC LIMIT ?";
        return jdbcTemplate.query(query, filmRowMapper, count);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String query = "SELECT * FROM genres";
        return jdbcTemplate.query(query, genreRowMapper);
    }

    @Override
    public Genre getGenre(Integer id) {
        String query = "SELECT * FROM genres WHERE id = ?";
        Genre result = jdbcTemplate.queryForObject(query, genreRowMapper, id);
        if (result == null) {
            throw new NotFoundException("Genre not found");
        }
        return result;
    }

    @Override
    public Collection<Rating> getAllRatings() {
        String query = "SELECT * FROM ratings";
        return jdbcTemplate.query(query, ratingRowMapper);
    }

    @Override
    public Rating getRating(Integer id) {
        String query = "SELECT * FROM ratings WHERE id = ?";
        Rating result = jdbcTemplate.queryForObject(query, ratingRowMapper, id);
        if (result == null) {
            throw new NotFoundException("Rating not found");
        }
        return result;
    }

    @Override
    public void addLike(Integer userId, Integer filmId) {
        String query = "INSERT INTO likes (user_id, film_id) VALUES (?,?)";
        jdbcTemplate.update(query, userId, filmId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        String query = "DELETE FROM likes WHERE user_id =? AND film_id= ?";
        jdbcTemplate.update(query, userId, filmId);
    }
}
