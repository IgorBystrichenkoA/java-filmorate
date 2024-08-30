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
        userStorage.addFriend(id1, id2);
    }

    public void deleteFriend(Integer id1, Integer id2) {
        log.info("delete friend {} {}", id1, id2);
        userStorage.deleteFriend(id1, id2);
    }

    public Collection<User> getMutualFriends(Integer id1, Integer id2) {
        log.info("get mutual friends {} {}", id1, id2);
        Collection<User> user1Friends = userStorage.getFriends(id1);
        Collection<User> user2Friends = userStorage.getFriends(id2);
        Set<User> resultSet = new HashSet<>(user1Friends);
        resultSet.retainAll(new HashSet<>(user2Friends));
        return resultSet;
    }
}
