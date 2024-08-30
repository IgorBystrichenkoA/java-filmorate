package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Film get(Integer id);

    Film update(Film film);

    Film delete(Film film);

    Collection<Film> getAllFilms();

    Collection<Film> getTopFilms(Integer count);

    Collection<Genre> getAllGenres();

    Genre getGenre(Integer id);

    Collection<Mpa> getAllRatings();

    Mpa getRating(Integer id);

    void addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);
}
