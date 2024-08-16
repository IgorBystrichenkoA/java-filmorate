package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer id1, Integer id2) {
        userStorage.get(id1).addFriend(id2);
        userStorage.get(id2).addFriend(id1);
    }

    public void deleteFriend(Integer id1, Integer id2) {
        userStorage.get(id1).deleteFriend(id2);
        userStorage.get(id2).deleteFriend(id1);
    }

    public Collection<User> getMutualFriends(Integer id1, Integer id2) {
        Set<Integer> resultSet = new HashSet<>(userStorage.get(id1).getFriends());
        resultSet.retainAll(userStorage.get(id2).getFriends());
        return resultSet.stream()
                .map(id -> userStorage.get(id))
                .toList();
    }

    public Collection<User> getFriends(Integer id) {
        return userStorage.get(id).getFriends().stream()
                .map(friendId -> userStorage.get(friendId))
                .toList();
    }
}
