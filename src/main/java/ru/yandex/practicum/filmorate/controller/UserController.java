package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Marker;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private int seq = 0;

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Creating user: {}", user);
        user.setId(generateId());
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    private int generateId() {
        return ++seq;
    }

    @PutMapping
    public User update(@Validated(Marker.OnUpdate.class) @RequestBody User user) {
        log.info("Updating user: {}", user);
        User userToUpdate = users.get(user.getId());
        if (userToUpdate == null) {
            log.error("User not found");
            throw new NotFoundException("User not found");
        }
        if (!StringUtils.hasText(user.getName())) {
            userToUpdate.setName(user.getLogin());
        } else {
            userToUpdate.setName(user.getName());
        }
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setLogin(user.getLogin());
        userToUpdate.setBirthday(user.getBirthday());
        return userToUpdate;
    }

    @GetMapping
    public Collection<User> getUsers() {
        log.info("Retrieving all users");
        return users.values();
    }
}
