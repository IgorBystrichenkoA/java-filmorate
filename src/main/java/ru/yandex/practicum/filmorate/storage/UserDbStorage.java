package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.rowMapper.UserRowMapper;

import java.util.Collection;

@Component("h2UserStorage")
public class UserDbStorage implements UserStorage {
    NamedParameterJdbcTemplate jdbc;
    UserRowMapper mapper;

    @Autowired
    public UserDbStorage(NamedParameterJdbcTemplate jdbc, UserRowMapper userRowMapper) {
        this.jdbc = jdbc;
        this.mapper = userRowMapper;
    }

    @Override
    public User create(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("email", user.getEmail());
        namedParams.addValue("login", user.getLogin());
        namedParams.addValue("name", user.getName());
        namedParams.addValue("birthday", user.getBirthday());
        String sqlQuery = "INSERT INTO users (email, login, name, birthday) VALUES (:email,:login,:name,:birthday)";
        jdbc.update(sqlQuery, namedParams, keyHolder, new String[]{"id"});
        user.setId(keyHolder.getKeyAs(Integer.class));
        return user;
    }

    @Override
    public User get(Integer id) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", id);
        String sqlQuery = "SELECT * FROM users WHERE id = :id";
        try {
            User result = jdbc.queryForObject(sqlQuery, namedParams, mapper);
            if (result == null) {
                throw new NotFoundException("User not found");
            }
            return result;
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("User not found");
        }
    }

    @Override
    public User update(User user) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", user.getId());
        namedParams.addValue("email", user.getEmail());
        namedParams.addValue("login", user.getLogin());
        namedParams.addValue("name", user.getName());
        namedParams.addValue("birthday", user.getBirthday());
        String sqlQuery = "UPDATE users SET " +
                "email = :email, login =:login, name =:name, birthday =:birthday " +
                "WHERE id = :id";
        int rows = jdbc.update(sqlQuery, namedParams);
        if (rows == 0) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    @Override
    public User delete(User user) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", user.getId());
        String sqlQuery = "DELETE FROM users WHERE id =:id";
        jdbc.update(sqlQuery, namedParams);
        return user;
    }

    @Override
    public Collection<User> getAll() {
        String sqlQuery = "SELECT * FROM users";
        return jdbc.query(sqlQuery, mapper);
    }

    @Override
    public Collection<User> getFriends(Integer id) {
        get(id);
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", id);
        String sqlQuery = "SELECT u.* FROM friends f1 JOIN friends f2 ON " +
                "f1.user_friend_id = f2.user_id AND " +
                "f2.user_friend_id = f1.user_id " +
                "JOIN users u ON u.id = f1.user_friend_id " +
                "WHERE f1.user_id = :id";
        return jdbc.query(sqlQuery, namedParams, mapper);
    }

    @Override
    public Collection<User> getUsersByIds(Collection<Integer> ids) {
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("ids", ids);
        String sqlQuery = "SELECT * FROM users WHERE id IN (:ids)";
        return jdbc.query(sqlQuery, namedParams, mapper);
    }

    @Override
    public void addFriend(Integer id, Integer friendId) {
        get(id); // проверяем, что пользователи существуют
        get(friendId);
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", id);
        namedParams.addValue("friendId", friendId);
        String sqlQuery = "INSERT INTO friends (user_id, user_friend_id) VALUES (:id,:friendId)";
        jdbc.update(sqlQuery, namedParams);
    }

    @Override
    public void deleteFriend(Integer id, Integer friendId) {
        get(id); // проверяем, что пользователи существуют
        get(friendId);
        MapSqlParameterSource namedParams = new MapSqlParameterSource();
        namedParams.addValue("id", id);
        namedParams.addValue("friendId", friendId);
        String sqlQuery = "DELETE FROM friends WHERE user_id = :id AND user_friend_id = :friendId";
        jdbc.update(sqlQuery, namedParams);
    }
}
