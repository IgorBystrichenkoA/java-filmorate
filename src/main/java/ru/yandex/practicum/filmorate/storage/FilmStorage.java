package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Film get(Integer id);

    Film update(Film film);

    Film delete(Film film);

    Collection<Film> getAllFilms();

    Collection<Film> getTopFilms(Integer count);
}
