package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new ConcurrentHashMap<>();
    private int seq = 0;

    @Override
    public User create(User user) {
        user.setId(generateId());
        if (!StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(Integer id) {
        User user = users.get(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    @Override
    public User update(User user) {
        User userToUpdate = users.get(user.getId());
        if (userToUpdate == null) {
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

    @Override
    public User delete(User user) {
        return users.remove(user.getId());
    }

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public Collection<User> getFriends(Integer id) {
        return get(id).getFriends().stream()
                .map(this::get)
                .toList();
    }

    @Override
    public Collection<User> getUsersByIds(Collection<Integer> ids) {
        return ids.stream()
                .map(this::get)
                .toList();
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {

    }

    @Override
    public void deleteFriend(Integer id1, Integer friendId) {

    }

    private int generateId() {
        return ++seq;
    }
}
