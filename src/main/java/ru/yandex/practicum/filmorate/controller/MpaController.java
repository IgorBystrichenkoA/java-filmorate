package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final FilmStorage filmStorage;

    @Autowired
    public MpaController(@Qualifier("h2FilmStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping("/{id}")
    public Mpa getGenre(@PathVariable int id) {
        return filmStorage.getRating(id);
    }

    @GetMapping
    public Collection<Mpa> getGenres() {
        return filmStorage.getAllRatings();
    }
}
