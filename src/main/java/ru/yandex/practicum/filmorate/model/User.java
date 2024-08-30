package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
    @NotNull(groups = Marker.OnUpdate.class)
    private Integer id;
    @Email
    private String email;
    @NotBlank @Pattern(regexp = "\\w+")
    private String login;
    private String name;
    @PastOrPresent
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Calendar birthday;

    private Set<Integer> friends = new HashSet<>();
    private Set<Integer> notConfirmedFriends = new HashSet<>();

    public User(Integer id, String email, String login, String name, Calendar birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend(Integer id) {
        friends.add(id);
    }

    public void deleteFriend(Integer id) {
        friends.remove(id);
    }
}
