package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.rowMapper.UserRowMapper;

import java.util.Collection;

@Component("h2UserStorage")
public class UserDbStorage implements UserStorage {
    JdbcTemplate jdbcTemplate;
    UserRowMapper mapper;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate, UserRowMapper userRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = userRowMapper;
    }

    @Override
    public User create(User user) {
        String query = "INSERT INTO users (email, login, name, birthday) VALUES (?,?,?,?)";
        jdbcTemplate.update(query, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return user;
    }

    @Override
    public User get(Integer id) {
        String query = "SELECT * FROM users WHERE id = ?";
        User result = jdbcTemplate.queryForObject(query, mapper, id);
        if (result == null) {
            throw new NotFoundException("User not found");
        }
        return result;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET " +
                "email =?, login =?, name =?, birthday =? " +
                "WHERE id = ?";
        jdbcTemplate.update(sqlQuery, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return user;
    }

    @Override
    public User delete(User user) {
        String sqlQuery = "DELETE FROM users WHERE id =?";
        jdbcTemplate.update(sqlQuery, user.getId());
        return user;
    }

    @Override
    public Collection<User> getAll() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, mapper);
    }

    @Override
    public Collection<User> getFriends(Integer id) {
        String sqlQuery = "SELECT u.* FROM friends AS f " +
                "INNER JOIN users AS u ON f.user_friend_id = u.id " +
                "WHERE f.user_id =?";
        return jdbcTemplate.query(sqlQuery, mapper, id);
    }

    @Override
    public Collection<User> getUsersByIds(Collection<Integer> ids) {
        String sqlQuery = "SELECT * FROM users WHERE id IN (?)";
        return jdbcTemplate.query(sqlQuery, mapper, ids);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        String sqlQuery = "INSERT INTO friends (user_id, user_friend_id) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_id =? AND film_id= ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }
}
