package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new TreeMap<>();
    private int seq = 0;

    @Override
    public Film create(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film get(int id) {
        Film film = films.get(id);
        if (film == null) {
            throw new NotFoundException("Film not found");
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        Film filmToUpdate = films.get(film.getId());
        if (filmToUpdate == null) {
            throw new NotFoundException("Film not found");
        }
        filmToUpdate.setName(film.getName());
        filmToUpdate.setDescription(film.getDescription());
        filmToUpdate.setReleaseDate(film.getReleaseDate());
        filmToUpdate.setDuration(film.getDuration());
        return filmToUpdate;
    }

    @Override
    public Film delete(Film film) {
        return films.remove(film.getId());
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    private int generateId() {
        return ++seq;
    }

}
