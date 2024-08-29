package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Genre {
    @NotNull(groups = Marker.OnUpdate.class)
    private Integer id;
    @NotNull
    private String name;
}
