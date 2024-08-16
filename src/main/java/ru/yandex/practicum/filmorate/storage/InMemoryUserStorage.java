package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
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
    public User get(int id) {
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

    private int generateId() {
        return ++seq;
    }
}
