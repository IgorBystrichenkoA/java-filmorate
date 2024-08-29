package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("h2UserStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Integer id1, Integer id2) {
        log.info("add friend {} {}", id1, id2);
        User user1 = userStorage.get(id1);
        User user2 = userStorage.get(id2);
        user1.addFriend(id2);
        user2.addFriend(id1);
        userStorage.update(user1);
        userStorage.update(user2);
    }

    public void deleteFriend(Integer id1, Integer id2) {
        log.info("delete friend {} {}", id1, id2);
        User user1 = userStorage.get(id1);
        User user2 = userStorage.get(id2);
        user1.deleteFriend(id2);
        user2.deleteFriend(id1);
        userStorage.update(user1);
        userStorage.update(user2);
    }

    public Collection<User> getMutualFriends(Integer id1, Integer id2) {
        log.info("get mutual friends {} {}", id1, id2);
        User user1 = userStorage.get(id1);
        User user2 = userStorage.get(id2);
        Set<Integer> resultSet = new HashSet<>(user1.getFriends());
        resultSet.retainAll(user2.getFriends());
        return userStorage.getUsersByIds(resultSet);
    }
}
