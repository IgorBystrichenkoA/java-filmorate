package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.rowMapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.rowMapper.GenreRowMapper;
import ru.yandex.practicum.filmorate.storage.rowMapper.RatingRowMapper;

import java.util.Collection;

@Component("h2FilmStorage")
public class FilmDbStorage implements FilmStorage {
    NamedParameterJdbcTemplate jdbc;
    UserStorage userStorage;
    FilmRowMapper filmRowMapper;
    GenreRowMapper genreRowMapper;
    RatingRowMapper ratingRowMapper;

    @Autowired
    public FilmDbStorage(NamedParameterJdbcTemplate jdbc,
                         @Qualifier("h2UserStorage") UserStorage userStorage,
                         FilmRowMapper filmRowMapper,
                         GenreRowMapper genreRowMapper,
                         RatingRowMapper ratingRowMapper) {
        this.jdbc = jdbc;
        this.userStorage = userStorage;
        this.filmRowMapper = filmRowMapper;
        this.genreRowMapper = genreRowMapper;
        this.ratingRowMapper = ratingRowMapper;
    }

    @Override
    public Film create(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("name", film.getName());
        namedParams.addValue("description", film.getDescription());
        namedParams.addValue("releaseDate", film.getReleaseDate());
        namedParams.addValue("duration", film.getDuration());
        namedParams.addValue("mpa", film.getMpa() == null ? null : film.getMpa().getId());

        String sqlQuery = "INSERT INTO films (name, description, releaseDate, duration, mpa) " +
                "VALUES (:name,:description,:releaseDate,:duration,:mpa)";
        jdbc.update(sqlQuery, namedParams, keyHolder, new String[] {"id"});
        film.setId(keyHolder.getKeyAs(Integer.class));
        return film;
    }

    @Override
    public Film get(Integer id) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", id);
        String sqlQuery = "SELECT f.*, m.name AS mpaName FROM films AS f " +
                "INNER JOIN mpa AS m ON f.mpa = m.id " +
                "WHERE f.id = :id";
        Film result = jdbc.queryForObject(sqlQuery, namedParams, filmRowMapper);
        if (result == null) {
            throw new NotFoundException("Film not found");
        }
        return result;
    }

    @Override
    public Film update(Film film) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", film.getId());
        namedParams.addValue("name", film.getName());
        namedParams.addValue("description", film.getDescription());
        namedParams.addValue("releaseDate", film.getReleaseDate());
        namedParams.addValue("duration", film.getDuration());
        namedParams.addValue("mpa", film.getMpa() == null ? null : film.getMpa().getId());
        String sqlQuery = "UPDATE films SET name = :name, description = :description, " +
                "releaseDate =:releaseDate, duration =:duration, mpa =:mpa " +
                "WHERE id = :id";
        int rows = jdbc.update(sqlQuery, namedParams, keyHolder, new String[] {"id"});
        if (rows == 0) {
            throw new NotFoundException("Film not found");
        }
        return film;
    }

    @Override
    public Film delete(Film film) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", film.getId());
        String sqlQuery = "DELETE FROM films WHERE id = :id";
        jdbc.update(sqlQuery, namedParams);
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sqlQuery = "SELECT f.*, m.name AS mpaName FROM films f " +
                "INNER JOIN mpa m ON f.mpa = m.id";
        return jdbc.query(sqlQuery, filmRowMapper);
    }

    @Override
    public Collection<Film> getTopFilms(Integer count) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("count", count);
        String sqlQuery = "SELECT COUNT(l.USER_ID) likes_count, f.*, m.name AS mpaName FROM FILMS AS f " +
                "LEFT JOIN LIKES AS l ON F.ID = L.FILM_ID " +
                "INNER JOIN mpa AS m ON f.mpa = m.id " +
                "GROUP BY f.ID " +
                "ORDER BY likes_count DESC " +
                "LIMIT :count;";
        return jdbc.query(sqlQuery, namedParams, filmRowMapper);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sqlQuery = "SELECT DISTINCT name, id FROM genres";
        return jdbc.query(sqlQuery, genreRowMapper);
    }

    @Override
    public Genre getGenre(Integer id) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", id);
        String sqlQuery = "SELECT * FROM genres WHERE id = :id";
        Genre result = jdbc.queryForObject(sqlQuery, namedParams, genreRowMapper);
        if (result == null) {
            throw new NotFoundException("Genre not found");
        }
        return result;
    }

    @Override
    public Collection<Mpa> getAllRatings() {
        String sqlQuery = "SELECT * FROM mpa";
        return jdbc.query(sqlQuery, ratingRowMapper);
    }

    @Override
    public Mpa getRating(Integer id) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", id);
        String sqlQuery = "SELECT * FROM mpa WHERE id = :id";
        Mpa result = jdbc.queryForObject(sqlQuery, namedParams, ratingRowMapper);
        if (result == null) {
            throw new NotFoundException("Rating not found");
        }
        return result;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        get(filmId);
        userStorage.get(userId);
        namedParams.addValue("filmId", filmId);
        namedParams.addValue("userId", userId);
        try {
            String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES(:filmId, :userId)";
            jdbc.update(sqlQuery, namedParams);
        } catch (DuplicateKeyException ignored) {
        }
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("userId", userId);
        namedParams.addValue("filmId", filmId);
        String sqlQuery = "DELETE FROM likes WHERE user_id = :userId AND film_id = :filmId";
        int rows = jdbc.update(sqlQuery, namedParams);
        if (rows == 0) {
            throw new NotFoundException("Like not found");
        }
    }
}
