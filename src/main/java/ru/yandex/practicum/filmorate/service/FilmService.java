package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Integer filmId, Integer userId) {
        log.info("Adding like for film {} by user {}", filmId, userId);
        userStorage.get(userId); // Если пользователя нет, вылетит NotFoundException
        Film filmToUpdate = filmStorage.get(filmId);
        filmToUpdate.addLike(userId);
        filmStorage.update(filmToUpdate);
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.info("Deleting like for film {} by user {}", filmId, userId);
        userStorage.get(userId);
        Film filmToUpdate = filmStorage.get(filmId);
        filmToUpdate.deleteLike(userId);
        filmStorage.update(filmToUpdate);
    }

    public Collection<Film> getTopFilms(Integer count) {
        log.info("Getting top {} films", count);
        return filmStorage.getTopFilms(count);
    }
}
