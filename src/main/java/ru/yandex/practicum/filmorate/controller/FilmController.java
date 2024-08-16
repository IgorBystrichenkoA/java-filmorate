package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Creating film: {}", film);
        return filmStorage.create(film);
    }

    @PutMapping
    public Film update(@Validated(Marker.OnUpdate.class) @RequestBody Film film) {
        log.info("Updating film: {}", film);
        return filmStorage.update(film);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Retrieving all films");
        return filmStorage.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Adding like");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Deleting like");
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Retrieving most popular films");
        return filmService.getTopFilms(count);
    }
}
