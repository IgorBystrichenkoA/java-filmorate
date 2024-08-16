package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.Comparator;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Integer filmId, Integer userId) {
        filmStorage.get(filmId).addLike(userId);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        filmStorage.get(filmId).deleteLike(userId);
    }

    public Collection<Film> getTopFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt(film -> film.getLikes().size()))
                .limit(count)
                .toList();
    }
}
