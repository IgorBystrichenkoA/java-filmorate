package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Creating user: {}", user);
        return userStorage.create(user);
    }

    @PutMapping
    public User update(@Validated(Marker.OnUpdate.class) @RequestBody User user) {
        log.info("Updating user: {}", user);
        return userStorage.update(user);
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Retrieving all users");
        return userStorage.getAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Adding friend");
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Deleting friend");
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable int id) {
        log.info("Retrieving all friends");
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Retrieving mutual friends");
        return userService.getMutualFriends(id, otherId);
    }
}
