package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new ConcurrentHashMap<>();
    private int seq = 0;

    @Override
    public Film create(Film film) {
        film.setId(generateId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film get(Integer id) {
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

    @Override
    public Collection<Film> getTopFilms(Integer count) {
        return getAllFilms().stream()
                    .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                    .limit(count)
                    .toList();
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return List.of();
    }

    @Override
    public Genre getGenre(Integer id) {
        return null;
    }

    @Override
    public Collection<Rating> getAllRatings() {
        return List.of();
    }

    @Override
    public Rating getRating(Integer id) {
        return null;
    }

    @Override
    public void addLike(Integer filmId, Integer userId) {

    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {

    }

    private int generateId() {
        return ++seq;
    }

}
