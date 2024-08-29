package ru.yandex.practicum.filmorate.storage.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setEmail(resultSet.getString("email"));
        user.setLogin(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        Date date = resultSet.getDate("birthday");
        if (date!= null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            user.setBirthday(cal);
        } else {
            user.setBirthday(null);
        }

        return user;
    }
}
