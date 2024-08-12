package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Marker;

import java.util.*;

@RestController
@RequestMapping("/api/v1/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int seq = 0;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Creating film: {}", film);
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    private int generateId() {
        return ++seq;
    }

    @PutMapping
    public Film update(@Validated(Marker.OnUpdate.class) @RequestBody Film film) {
        log.info("Updating film: {}", film);
        Film filmToUpdate = films.get(film.getId());
        if (filmToUpdate == null) {
            log.error("Film not found");
            return null;
        }
        filmToUpdate.setName(film.getName());
        filmToUpdate.setDescription(film.getDescription());
        filmToUpdate.setReleaseDate(film.getReleaseDate());
        filmToUpdate.setDuration(film.getDuration());
        return filmToUpdate;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Retrieving all films");
        return films.values();
    }
}
