package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Calendar;

@Data
@AllArgsConstructor
public class User {
    @NotNull(groups = Marker.OnUpdate.class)
    private Integer id;
    @Email
    private String email;
    @NotBlank @Pattern(regexp = "\\w+")
    private String login;
    private String name;
    @PastOrPresent
    private Calendar birthday;
}
