package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User create(User user);

    User get(Integer id);

    User update(User user);

    User delete(User user);

    Collection<User> getAll();

    Collection<User> getFriends(Integer id);

    Collection<User> getUsersByIds(Collection<Integer> ids);

    void addFriend(Integer id, Integer friendId);

    void deleteFriend(Integer id1, Integer friendId);

    Collection<User> getConfirmedFriends(Integer id);
}
