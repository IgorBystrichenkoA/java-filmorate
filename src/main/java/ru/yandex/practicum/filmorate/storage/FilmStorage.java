package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film create(Film film);

    Film get(int id);

    Film update(Film film);

    Film delete(Film film);

    Collection<Film> getAllFilms();
}
