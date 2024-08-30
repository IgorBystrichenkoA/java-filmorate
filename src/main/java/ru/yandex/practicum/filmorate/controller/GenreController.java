package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@RestController
@RequestMapping("/genres")
public class GenreController {

    private final FilmStorage filmStorage;

    @Autowired
    public GenreController(@Qualifier("h2FilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping("/{id}")
    public Genre getGenre(@PathVariable int id) {
        return filmStorage.getGenre(id);
    }

    @GetMapping
    public Collection<Genre> getGenres() {
        return filmStorage.getAllGenres();
    }
}
